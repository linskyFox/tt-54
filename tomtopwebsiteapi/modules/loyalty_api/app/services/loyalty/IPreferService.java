package services.loyalty;

import java.util.List;

import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import context.WebContext;

/**
 * 优惠活动的总控服务类
 * 
 * @author lijun
 *
 */
public interface IPreferService {

	/**
	 * 用户所有的优惠汇总
	 */
	public List<LoyaltyPrefer> getAllPreferByEmail(String email,
			List<CartItem> cartItems, WebContext webCtx);

	/**
	 * 订单成功付款后把相应的优惠信息设置为已用
	 * 
	 * @param email
	 * @param cartItems
	 * @param webCtx
	 * @return
	 */
	public boolean saveAllPrefer(String email,
			List<LoyaltyPrefer> loyaltyPrefers, WebContext webCtx);

}
