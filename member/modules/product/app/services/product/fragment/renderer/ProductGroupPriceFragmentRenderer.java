package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class ProductGroupPriceFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_group_price
				.render((valueobjects.product.ProductGroupPrice) fragment);
	}

}
