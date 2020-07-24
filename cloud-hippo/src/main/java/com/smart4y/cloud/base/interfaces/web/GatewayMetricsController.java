package com.smart4y.cloud.base.interfaces.web;
//
//import com.smart4y.cloud.core.domain.message.ResultMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
///**
// * 网关监控数据
// * "/metrics"
// *
// * @author Youtao
// * on 2020/7/22 15:58
// */
//@Slf4j
//@RestController
//public class GatewayMetricsController {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private static final String UPTIME = "uptime";
//    private static final String MEM = "mem";
//    private static final String MEM_FREE = "mem.free";
//    private static final String HEAP = "heap";
//    private static final String HEAP_COMMITTED = "heap.committed";
//    private static final String HEAP_INIT = "heap.init";
//    private static final String HEAP_USED = "heap.used";
//    private static final String NON_HEAP_COMMITTED = "nonheap.committed";
//    private static final String NON_HEAP_INIT = "nonheap.init";
//    private static final String NON_HEAP_USED = "nonheap.used";
//    private static final String NON_HEAP = "nonheap";
//    private static final String PROCESSORS = "processors";
//    private static final String SYSTEM_LOAD_AVERAGE = "systemload.average";
//    private static final String THREADS_PEAK = "threads.peak";
//    private static final String THREADS_DAEMON = "threads.daemon";
//    private static final String THREADS_TOTAL_STARTED = "threads.totalStarted";
//    private static final String THREADS = "threads";
//    private static final String CLASSES = "classes";
//    private static final String CLASSES_LOADED = "classes.loaded";
//    private static final String CLASSES_UNLOADED = "classes.unloaded";
//    private static final String GC_PARNEW_COUNT = "gc.parnew.count";
//    private static final String GC_PARNEW_TIME = "gc.parnew.time";
//    private static final String GC_CONCURRENTMARKSWEEP_COUNT = "gc.concurrentmarksweep.count";
//    private static final String GC_CONCURRENTMARKSWEEN_TIME = "gc.concurrentmarksweep.time";
//    private static final String KEEP_ALIVE_COUNT = "keepAliveCount";
//    private static final String MAX_CONNECTIONS = "maxConnections";
//
//    @GetMapping("/gateway/metrics")
//    public ResultMessage<String> metrics() {
//        return ResultMessage.ok();
//    }
//}