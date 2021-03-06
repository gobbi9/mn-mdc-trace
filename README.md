## Reproducer for missing traceId and spanId MDC with Jaeger + Micronaut

### Instructions

```bash
docker-compose up
```

#### Send a request to the zipkin application, and check the logs

```bash
curl http://localhost:8098/test/1
```

```text
mn-zipkin    | 06:33:23.048 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestController - MDC:[traceId=da93572693f61142, spanId=da93572693f61142, spanExportable=true, X-Span-Export=true, test=test value, X-B3-SpanId=da93572693f61142, X-B3-TraceId=da93572693f61142] - controller test # 1!
mn-zipkin    | 06:33:23.048 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestService - MDC:[traceId=da93572693f61142, spanId=42c40d106f002b14, spanExportable=true, X-Span-Export=true, test=test value, X-B3-SpanId=42c40d106f002b14, X-B3-ParentSpanId=da93572693f61142, X-B3-TraceId=da93572693f61142, parentId=da93572693f61142] - service test # 1!
```

#### Send a request to the jaeger application, and check the logs

```bash
curl http://localhost:8099/test/2
```

```text
mn-jaeger    | 06:34:00.033 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestController - MDC:[test=test value] - controller test # 2!
mn-jaeger    | 06:34:00.034 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestService - MDC:[test=test value] - service test # 2!
```

traceId and spanId are not in MDC.

#### With the fixed version from branch `fixed`

```bash
curl http://localhost:8100/test/3
```

```text
mn-jaeger-fixed    | 10:49:14.132 [default-nioEventLoopGroup-1-2] INFO  com.example.main.TestController - MDC:[traceId=5c6dc6d99e8949e7, spanId=5c6dc6d99e8949e7, test=test value, sentToJaeger=true] - controller test # 3!
mn-jaeger-fixed    | 10:49:14.137 [default-nioEventLoopGroup-1-2] INFO  com.example.main.TestService - MDC:[traceId=5c6dc6d99e8949e7, spanId=40a6cfe8b9213c8e, test=test value, sentToJaeger=true] - service test # 3!
```
