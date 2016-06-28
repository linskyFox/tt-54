package services.home.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.ProductAdvertisingCompositeEnquiry;
import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;

public class HomeAdHeadProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "advert-home-head";
	}

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingCompositeEnquiry;
	@Inject
	FoundationService foundation;

	@Override
	public Html getFragment(Context ctx) {
		int siteid = 1;
		int lang = 1;
		String device = "web";
		if (ctx != null) {
			siteid = foundation.getSiteID(ctx);
			lang = foundation.getLanguage(ctx);
			device = foundation.getDevice();
		}
		ProductAdertisingContext context = new ProductAdertisingContext(null,
				5, siteid, lang, 6, device);

		List<AdItem> list = productAdvertisingCompositeEnquiry
				.getAdvertisings(context);
		return views.html.home.head_advert.render(list);
	}

}
