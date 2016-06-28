package services.base;

import java.util.List;
import java.util.Map;

import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;

import extensions.InjectorInstance;
import play.Logger;
import play.Play;
import services.IConfigService;
import services.base.template.TemplateService;

public class ConfigService implements IConfigService {

	@Override
	public String getConfig(String key) {
		if (key == null || key.length() == 0) {
			return null;
		}
		return Play.application().configuration().getString(key);
	}

	@Override
	public Map<String, String> getConfig(List<String> keys) {

		if (keys == null || keys.size() == 0) {
			Logger.debug("keys is null");
			return null;
		}
		Map<String, String> feedback = Maps.newHashMap();

		FluentIterable.from(keys).forEach(k -> {
			Logger.debug("key:{}", k);
			feedback.put(k, Play.application().configuration().getString(k));
		});
		return feedback;
	}

	public static String getLivechatUrl() {
		try {
			String value = Play.application().configuration()
					.getString("livechat.url");
			return value;
		} catch (Exception ex) {
			Logger.error("get showlivechart config error: ", ex);
		}
		return "#";
	}

}
