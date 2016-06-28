package extensions.base.tracking;


import javax.inject.Inject;
import javax.inject.Singleton;

import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ISystemParameterService;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

@Singleton
public class GoogleTagManagerScriptAddition implements IFilter {


	@Override
	public int priority() {
		return 99;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		String gtm = "GTM-M9TSMK";
		views.html.base.tracking.gtm.render(gtm, context);
		return chain.executeNext(context);
	}

}
