package com.aliyun.dts.deliver.test;

import com.aliyun.dts.deliver.base.Destination;
import com.aliyun.dts.deliver.base.Source;
import com.aliyun.dts.deliver.commons.config.GlobalSettings;
import com.aliyun.dts.deliver.commons.config.JobConfig;
import com.aliyun.dts.deliver.connector.desination.DStoreDestination;
import com.aliyun.dts.deliver.core.bootstrap.DtsDeliver;
import com.aliyun.dts.deliver.protocol.record.checkpoint.RecordCheckpoint;
import com.aliyun.dts.deliver.resolver.internal.RecordGroupSender;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DtsDeliverTest {
    private static final Logger LOG = LoggerFactory.getLogger(DtsDeliverTest.class);

    public static void main(String[] args) throws Throwable  {

        String configPath = "";
        Map<String, String> settingValueMap = new HashMap<>();

        settingValueMap.put(GlobalSettings.DTS_BOOTSTRAP_SERVERS_CONFIG.getKey(), "dts_proxy_url");
        settingValueMap.put(GlobalSettings.ALIYUN_AK.getKey(), "ak");
        settingValueMap.put(GlobalSettings.ALIYUN_SECRET.getKey(), "secret");
        settingValueMap.put(GlobalSettings.DTS_JOB_ID.getKey(), "dts_instance_id");
        settingValueMap.put(GlobalSettings.DTS_DELIVER_TOPIC.getKey(), "topic");
        settingValueMap.put(GlobalSettings.DTS_DELIVER_TOPIC_PARTITION_NUM.getKey(), "3");
        settingValueMap.put(GlobalSettings.DTS_OPENAPI_REGION.getKey(), "cn-hangzhou");

        JobConfig jobConfig = new JobConfig(configPath, settingValueMap);

        //source
        List<Source> sourceList = new ArrayList<>();
        Source source1 = new FakeSource("source 1", jobConfig.getSettings());
        Source source2 = new FakeSource("source 2", jobConfig.getSettings());
        Source source3 = new FakeSource("source 3", jobConfig.getSettings());
        sourceList.add(source1);
        sourceList.add(source2);
        sourceList.add(source3);

        Destination destination = new DStoreDestination();

        Consumer<List<Pair<String, RecordCheckpoint>>> checkpointConsumer = (checkpoints) ->{
            saveCheckPoints(checkpoints);
        };

        DtsDeliver dtsDeliver = new DtsDeliver(jobConfig, sourceList, destination, checkpointConsumer);

        dtsDeliver.startup();

        LOG.info("sleep...");
        Thread.sleep(10 * 1000);

        dtsDeliver.stop();

        LOG.info("stoped");
    }

    //todo, such as file or database to save the checkpoint
    private static void saveCheckPoints(List<Pair<String, RecordCheckpoint>> checkpoints) {
        LOG.info("checkpoints: " + checkpoints);
    }
}
