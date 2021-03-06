package extensions.order;

import java.util.List;

import context.WebContext;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;

public interface IOrderLoyaltyProvider {
	public abstract LoyaltyPrefer applyCoupon(String email, String code);

	public abstract LoyaltyPrefer applyPromo(String email, String code);

	public abstract LoyaltyPrefer getCurrentPrefer(String email);

	/**
	 * 取消应用优惠
	 * 
	 * @param email
	 * @param code
	 * @return
	 */
	public abstract void undoCurrentPrefer();

	public abstract void undoCurrentPoint();

	public abstract LoyaltyPrefer applyPoints(String email, Integer costpoints);

	public abstract List<LoyaltyPrefer> getAllCurrentPrefer();

	public abstract List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx);

	public abstract int getMyUsablePoint(String email);

	public abstract LoyaltyPrefer applyCoupon(String cartId, String email,
			String code);

	public abstract LoyaltyPrefer applyPromo(String cartId, String email,
			String code);

	public abstract LoyaltyPrefer getCurrentPrefer(String cartId, String email);

	/**
	 * 取消应用优惠
	 * 
	 * @param email
	 * @param code
	 * @return
	 */

	public abstract LoyaltyPrefer applyPoints(String cartId, String email,
			Integer costpoints);

	public abstract List<LoyaltyPrefer> getAllCurrentPrefer(String cartId);

	public abstract List<LoyaltyCoupon> getMyUsableCoupon(String email,
			String cartId, WebContext webCtx);

	public abstract Double getAllCurrentPreferValue(String cartId);
}
