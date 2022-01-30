## Reproducer for missing traceId and spanId MDC with Jaeger + Micronaut

### Instructions

```bash
docker-compose up
```

Send a request to zipkin application, and check the logs:

```bash
curl http://localhost:8098/test/1
```

```text
mn-zipkin    | 06:33:23.048 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestController - MDC:[traceId=da93572693f61142, spanId=da93572693f61142, spanExportable=true, X-Span-Export=true, test=test value, X-B3-SpanId=da93572693f61142, X-B3-TraceId=da93572693f61142] - controller test # 1!
mn-zipkin    | 06:33:23.048 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestService - MDC:[traceId=da93572693f61142, spanId=42c40d106f002b14, spanExportable=true, X-Span-Export=true, test=test value, X-B3-SpanId=42c40d106f002b14, X-B3-ParentSpanId=da93572693f61142, X-B3-TraceId=da93572693f61142, parentId=da93572693f61142] - service test # 1!
```

Send a request to the jaeger application, and check the logs:

```bash
curl http://localhost:8099/test/2
```

```text
mn-jaeger    | 06:34:00.033 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestController - MDC:[test=test value] - controller test # 2!
mn-jaeger    | 06:34:00.034 [default-nioEventLoopGroup-1-3] INFO  com.example.main.TestService - MDC:[test=test value] - service test # 2!
```

traceId and spanId are not in MDC.

### Custom JagerFactory

`io.jaegertracing:jaeger-thrift` needs to be a compile time dependency which causes the error on startup:

```text
09:27:58.438 [main] ERROR io.micronaut.runtime.Micronaut - MDC:[] - Error starting Micronaut server: Bean definition [io.micronaut.reactive.reactor.instrument.ReactorInstrumentation] could not be loaded: Failed to inject value for parameter [tracer] of class: io.micronaut.tracing.instrument.util.OpenTracingInvocationInstrumenter

Message: Multiple possible bean candidates found: [io.opentracing.Tracer, io.opentracing.Tracer]
Path Taken: ReactorInstrumentation.init([ReactorInstrumenterFactory instrumenterFactory]) --> new ReactorInstrumenterFactory([List reactiveInvocationInstrumenterFactories]) --> new OpenTracingInvocationInstrumenter([Tracer tracer])
io.micronaut.context.exceptions.BeanInstantiationException: Bean definition [io.micronaut.reactive.reactor.instrument.ReactorInstrumentation] could not be loaded: Failed to inject value for parameter [tracer] of class: io.micronaut.tracing.instrument.util.OpenTracingInvocationInstrumenter

Message: Multiple possible bean candidates found: [io.opentracing.Tracer, io.opentracing.Tracer]
Path Taken: ReactorInstrumentation.init([ReactorInstrumenterFactory instrumenterFactory]) --> new ReactorInstrumenterFactory([List reactiveInvocationInstrumenterFactories]) --> new OpenTracingInvocationInstrumenter([Tracer tracer])
	at io.micronaut.context.DefaultBeanContext.initializeContext(DefaultBeanContext.java:1938)
	at io.micronaut.context.DefaultApplicationContext.initializeContext(DefaultApplicationContext.java:237)
	at io.micronaut.context.DefaultBeanContext.readAllBeanDefinitionClasses(DefaultBeanContext.java:3453)
	at io.micronaut.context.DefaultBeanContext.finalizeConfiguration(DefaultBeanContext.java:3883)
	at io.micronaut.context.DefaultBeanContext.start(DefaultBeanContext.java:329)
	at io.micronaut.context.DefaultApplicationContext.start(DefaultApplicationContext.java:183)
	at io.micronaut.runtime.Micronaut.start(Micronaut.java:72)
	at io.micronaut.runtime.Micronaut.run(Micronaut.java:313)
	at io.micronaut.runtime.Micronaut.run(Micronaut.java:299)
	at com.example.Application.main(Application.java:8)
Caused by: io.micronaut.context.exceptions.DependencyInjectionException: Failed to inject value for parameter [tracer] of class: io.micronaut.tracing.instrument.util.OpenTracingInvocationInstrumenter

Message: Multiple possible bean candidates found: [io.opentracing.Tracer, io.opentracing.Tracer]
Path Taken: ReactorInstrumentation.init([ReactorInstrumenterFactory instrumenterFactory]) --> new ReactorInstrumenterFactory([List reactiveInvocationInstrumenterFactories]) --> new OpenTracingInvocationInstrumenter([Tracer tracer])
	at io.micronaut.context.AbstractInitializableBeanDefinition.resolveBean(AbstractInitializableBeanDefinition.java:1955)
	at io.micronaut.context.AbstractInitializableBeanDefinition.getBeanForConstructorArgument(AbstractInitializableBeanDefinition.java:1189)
	at io.micronaut.tracing.instrument.util.$OpenTracingInvocationInstrumenter$Definition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:2336)
	at io.micronaut.context.DefaultBeanContext.addCandidateToList(DefaultBeanContext.java:3685)
	at io.micronaut.context.DefaultBeanContext.getBeanRegistrations(DefaultBeanContext.java:3591)
	at io.micronaut.context.DefaultBeanContext.getBeansOfType(DefaultBeanContext.java:1376)
	at io.micronaut.context.AbstractInitializableBeanDefinition.resolveBeansOfType(AbstractInitializableBeanDefinition.java:2030)
	at io.micronaut.context.AbstractInitializableBeanDefinition.getBeansOfTypeForConstructorArgument(AbstractInitializableBeanDefinition.java:1330)
	at io.micronaut.reactive.reactor.instrument.$ReactorInstrumentation$ReactorInstrumenterFactory$Definition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:2336)
	at io.micronaut.context.DefaultBeanContext.createAndRegisterSingletonInternal(DefaultBeanContext.java:3281)
	at io.micronaut.context.DefaultBeanContext.createAndRegisterSingleton(DefaultBeanContext.java:3267)
	at io.micronaut.context.DefaultBeanContext.getBeanForDefinition(DefaultBeanContext.java:2820)
	at io.micronaut.context.DefaultBeanContext.getBeanInternal(DefaultBeanContext.java:2782)
	at io.micronaut.context.DefaultBeanContext.getBean(DefaultBeanContext.java:1638)
	at io.micronaut.context.AbstractInitializableBeanDefinition.resolveBean(AbstractInitializableBeanDefinition.java:1933)
	at io.micronaut.context.AbstractInitializableBeanDefinition.getBeanForMethodArgument(AbstractInitializableBeanDefinition.java:1047)
	at io.micronaut.reactive.reactor.instrument.$ReactorInstrumentation$Definition.initialize(Unknown Source)
	at io.micronaut.reactive.reactor.instrument.$ReactorInstrumentation$Definition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:2336)
	at io.micronaut.context.DefaultBeanContext.createAndRegisterSingletonInternal(DefaultBeanContext.java:3281)
	at io.micronaut.context.DefaultBeanContext.loadContextScopeBean(DefaultBeanContext.java:2664)
	at io.micronaut.context.DefaultBeanContext.initializeContext(DefaultBeanContext.java:1932)
	... 9 common frames omitted
Caused by: io.micronaut.context.exceptions.NonUniqueBeanException: Multiple possible bean candidates found: [io.opentracing.Tracer, io.opentracing.Tracer]
	at io.micronaut.context.DefaultBeanContext.findConcreteCandidate(DefaultBeanContext.java:2429)
	at io.micronaut.context.DefaultApplicationContext.findConcreteCandidate(DefaultApplicationContext.java:455)
	at io.micronaut.context.DefaultBeanContext.lastChanceResolve(DefaultBeanContext.java:3253)
	at io.micronaut.context.DefaultBeanContext.findConcreteCandidateNoCache(DefaultBeanContext.java:3140)
	at io.micronaut.context.DefaultBeanContext.findConcreteCandidate(DefaultBeanContext.java:3054)
	at io.micronaut.context.DefaultBeanContext.getBeanInternal(DefaultBeanContext.java:2750)
	at io.micronaut.context.DefaultBeanContext.getBean(DefaultBeanContext.java:1638)
	at io.micronaut.context.AbstractInitializableBeanDefinition.resolveBean(AbstractInitializableBeanDefinition.java:1933)
	... 32 common frames omitted
```