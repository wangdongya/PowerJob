import com.github.kfcfans.powerjob.client.OhMyClient;
import com.github.kfcfans.powerjob.common.ExecuteType;
import com.github.kfcfans.powerjob.common.ProcessorType;
import com.github.kfcfans.powerjob.common.TimeExpressionType;
import com.github.kfcfans.powerjob.common.model.PEWorkflowDAG;
import com.github.kfcfans.powerjob.common.request.http.SaveJobInfoRequest;
import com.github.kfcfans.powerjob.common.request.http.SaveWorkflowRequest;
import com.github.kfcfans.powerjob.common.utils.JsonUtils;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 测试 Client（workflow部分）
 *
 * @author tjq
 * @since 2020/6/2
 */
public class TestWorkflow {

    private static OhMyClient ohMyClient;

    @BeforeAll
    public static void initClient() throws Exception {
        ohMyClient = new OhMyClient("127.0.0.1:7700", "powerjob-agent-test", "123");
    }

    @Test
    public void initTestData() throws Exception {
        SaveJobInfoRequest base = new SaveJobInfoRequest();
        base.setJobName("DAG-Node-");
        base.setTimeExpressionType(TimeExpressionType.WORKFLOW);
        base.setExecuteType(ExecuteType.STANDALONE);
        base.setProcessorType(ProcessorType.EMBEDDED_JAVA);
        base.setProcessorInfo("com.github.kfcfans.powerjob.samples.workflow.WorkflowStandaloneProcessor");

        for (int i = 0; i < 5; i++) {
            SaveJobInfoRequest request = JsonUtils.parseObject(JsonUtils.toBytes(base), SaveJobInfoRequest.class);
            request.setJobName(request.getJobName() + i);
            System.out.println(ohMyClient.saveJob(request));
        }
    }

    @Test
    public void testSaveWorkflow() throws Exception {

        // DAG 图
        List<PEWorkflowDAG.Node> nodes = Lists.newLinkedList();
        List<PEWorkflowDAG.Edge> edges = Lists.newLinkedList();

        nodes.add(new PEWorkflowDAG.Node(1L, "DAG-Node-1"));
        nodes.add(new PEWorkflowDAG.Node(2L, "DAG-Node-2"));

        edges.add(new PEWorkflowDAG.Edge(1L, 2L));

        PEWorkflowDAG peWorkflowDAG = new PEWorkflowDAG(nodes, edges);
        SaveWorkflowRequest req = new SaveWorkflowRequest();

        req.setWfName("workflow-by-client");
        req.setWfDescription("created by client");
        req.setPEWorkflowDAG(peWorkflowDAG);
        req.setEnable(true);
        req.setTimeExpressionType(TimeExpressionType.API);

        System.out.println(ohMyClient.saveWorkflow(req));
    }

    @Test
    public void testDisableWorkflow() throws Exception {
        System.out.println(ohMyClient.disableWorkflow(4L));
    }

    @Test
    public void testDeleteWorkflow() throws Exception {
        System.out.println(ohMyClient.deleteWorkflow(4L));
    }

    @Test
    public void testEnableWorkflow() throws Exception {
        System.out.println(ohMyClient.enableWorkflow(4L));
    }

    @Test
    public void testFetchWorkflowInfo() throws Exception {
        System.out.println(ohMyClient.fetchWorkflow(5L));
    }

    @Test
    public void testRunWorkflow() throws Exception {
        System.out.println(ohMyClient.runWorkflow(5L));
    }

    @Test
    public void testStopWorkflowInstance() throws Exception {
        System.out.println(ohMyClient.stopWorkflowInstance(149962433421639744L));
    }

    @Test
    public void testFetchWfInstanceInfo() throws Exception {
        System.out.println(ohMyClient.fetchWorkflowInstanceInfo(149962433421639744L));
    }

    @Test
    public void testRunWorkflowPlus() throws Exception {
        System.out.println(ohMyClient.runWorkflow(1L, "this is init Params 2", 90000));
    }
}
