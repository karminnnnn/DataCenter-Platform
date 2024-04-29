package net.srt.api;

import net.srt.api.flink114.Flink114Api;
import net.srt.api.flink116.Flink116Api;
import net.srt.flink.common.context.SpringContextUtils;
import net.srt.framework.common.exception.ServerException;

/**
 * @ClassName FlinkApiFactory
 * @Author zrx
 * @Date 2023/12/5 16:48
 */
public class FlinkApiFactory {
	public static DevelopFlinkApi getByVersion(FlinkVersion flinkVersion) {
		if (FlinkVersion.FLINK114.equals(flinkVersion)) {
			return SpringContextUtils.getBeanByClass(Flink114Api.class);
		} else if (FlinkVersion.FLINK116.equals(flinkVersion)) {
			return SpringContextUtils.getBeanByClass(Flink116Api.class);
		}
		throw new ServerException("Unsupported Flink Version");
	}
}
