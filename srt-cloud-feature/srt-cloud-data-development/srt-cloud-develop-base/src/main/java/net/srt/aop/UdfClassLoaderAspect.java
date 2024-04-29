/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package net.srt.aop;

import lombok.extern.slf4j.Slf4j;
import net.srt.flink.common.classloader.DinkyClassLoader;
import net.srt.flink.common.context.DinkyClassLoaderContextHolder;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.process.exception.FlinkProcessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UdfClassLoaderAspect {

	@Pointcut("execution(* net.srt.service.DataProductionTaskService.*(..))")
	public void taskServicePointcut() {
	}

	@Pointcut("taskServicePointcut()")
	public void allPointcut() {
	}

	@Around("allPointcut()")
	public Object round(ProceedingJoinPoint proceedingJoinPoint) {
		Object proceed = null;

		try {
			proceed = proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			throw new FlinkProcessException(e);
		} finally {
			if (proceed instanceof JobResult) {
				ClassLoader lastContextClassLoader = Thread.currentThread().getContextClassLoader();
				if (lastContextClassLoader instanceof DinkyClassLoader) {
					DinkyClassLoaderContextHolder.clear();
				}
			}
		}
		return proceed;

	}

}
