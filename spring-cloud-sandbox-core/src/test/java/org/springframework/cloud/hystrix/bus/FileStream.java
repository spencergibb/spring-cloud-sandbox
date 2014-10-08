package org.springframework.cloud.hystrix.bus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.netflix.turbine.aggregator.InstanceKey;
import com.netflix.turbine.aggregator.StreamAggregator;
import com.netflix.turbine.aggregator.TypeAndNameKey;
import com.netflix.turbine.internal.JsonUtility;

import rx.Observable;
import rx.observables.GroupedObservable;

public class FileStream {

    public static final String STREAM_ALL = "hystrixbus";

    public static void main(String[] args) {
        getHystrixStreamFromFile(STREAM_ALL, 1).flatMap(commandGroup -> commandGroup.take(50)).take(50).toBlocking().forEach(s -> System.out.println("s: " + s));
    }

    // a hack to simulate a stream
    public static Observable<GroupedObservable<TypeAndNameKey, Map<String, Object>>> getHystrixStreamFromFile(final String stream, final int latencyBetweenEvents) {
        Observable<GroupedObservable<InstanceKey, Map<String, Object>>> observable = Observable.create(sub -> {
            try {
                while (!sub.isUnsubscribed()) {
                    String packagePath = FileStream.class.getPackage().getName().replace('.', '/');
                    InputStream file = FileStream.class.getResourceAsStream("/" + packagePath + "/" + stream + ".stream");
                    BufferedReader in = new BufferedReader(new InputStreamReader(file));
                    String line = null;
                    while ((line = in.readLine()) != null && !sub.isUnsubscribed()) {
                        if (!line.trim().equals("")) {
                            if (line.trim().startsWith("{")) {
                                String json = line.trim();
                                try {
                                    Map<String, Object> jsonMap = JsonUtility.jsonToMap(json);
                                    Map<String, Object> origin = (Map<String, Object>) jsonMap.get("origin");
                                    //TODO: instanceid template
                                    //String instanceId = origin.get("serviceId")+":"+origin.get("host")+":"+origin.get("port");
                                    String instanceId = origin.get("serviceId") + ":" + origin.get("ipAddress") + ":" + origin.get("port");
                                    Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");

                                    data.put("instanceId", instanceId);
                                    sub.onNext(data);
                                    Thread.sleep(latencyBetweenEvents);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                sub.onError(e);
            }
        }).groupBy(data -> InstanceKey.create((String) data.get("instanceId")));
        return StreamAggregator.aggregateGroupedStreams(observable);
    }

    /*public static GroupedObservable<InstanceKey, Map<String, Object>> getHystrixStreamFromFileEachLineScheduledEvery10Milliseconds(final String stream, final int instanceID, final TestScheduler scheduler, int maxTime) {
        TestSubject<Map<String, Object>> scheduledOrigin = TestSubject.create(scheduler);
        try {
            String packagePath = FileStream.class.getPackage().getName().replace('.', '/');
            InputStream file = FileStream.class.getResourceAsStream("/" + packagePath + "/" + stream + ".stream");
            BufferedReader in = new BufferedReader(new InputStreamReader(file));
            String line = null;
            int time = 0;
            while ((line = in.readLine()) != null && time < maxTime) {
                if (!line.trim().equals("")) {
                    if (line.startsWith("data: ")) {
                        time = time + 10; // increment by 10 milliseconds
                        String json = line.substring(6);
                        try {
                            Map<String, Object> jsonMap = JsonUtility.jsonToMap(json);
                            //                            System.err.println(instanceID + " => scheduling at time: " + time + " => " + jsonMap);
                            scheduledOrigin.onNext(jsonMap, time);
                        } catch (Exception e) {
                            System.err.println("bad data");
                        }
                    }
                }
            }
            scheduledOrigin.onCompleted(maxTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return createGroupedObservable(InstanceKey.create(instanceID), scheduledOrigin.subscribeOn(scheduler));
    }*/
}