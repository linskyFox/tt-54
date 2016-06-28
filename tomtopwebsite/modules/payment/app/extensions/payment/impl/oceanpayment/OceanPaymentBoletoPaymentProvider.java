package extensions.payment.impl.oceanpayment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.PaymentAccountMapper;
import play.Configuration;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;
import services.member.login.LoginService;
import services.order.OrderUpdateService;
import services.payment.OceanPaymentService;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.payment.PaymentContext;

import com.google.api.client.util.Maps;

import dto.order.Order;
import dto.order.OrderDetail;
import enums.OrderLableEnum;
import extensions.payment.IPaymentProvider;

public class OceanPaymentBoletoPaymentProvider implements IPaymentProvider {

	@Inject
	private PaymentAccountMapper accountMapper;
	@Inject
	private OceanPaymentService paymentService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private FoundationService foundation;
	@Inject
	private OrderUpdateService updateService;
	@Inject
	private LoginService loginService;

	@Override
	public String id() {
		return "oceanpayment_boleto";
	}

	@Override
	public String name() {
		return "Boleto";
	}

	@Override
	public int getDisplayOrder() {
		return 500;
	}

	@Override
	public String iconUrl() {
		return controllers.payment.impl.routes.Assets.at(
				"/images/payment05.png").url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		MemberInSession mis = loginService.getLoginData();
		if (context == null || context.isModeNew()) {
			return views.html.payment.oceanpayment.v2.boleto_description
					.render(mis);
		} else {
			return views.html.payment.oceanpayment.boleto_description
					.render(mis);
		}

	}

	@Override
	public boolean isManualProcess() {
		return false;
	}

	@Override
	public Html getInstruction(PaymentContext context) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Html getDoPaymentHtml(PaymentContext context) {
		Order order = context.getOrder().getOrder();
		String ccy = foundation.getCurrency();
		String accountString = accountMapper.getAccount(order.getIwebsiteid(),
				currencyService.exchange(order.getFgrandtotal(), ccy, "USD"),
				order.getCpaymentid());
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JsonFormatUtils.jsonToBean(
				accountString, LinkedHashMap.class);
		LinkedHashMap<String, String> form = parseToForm(order, account,
				context.getMap(), context.getOrder().getDetails(),
				context.getBackUrl());
		Configuration config = Play.application().configuration()
				.getConfig("oceanpayment");
		boolean isSandbox = config.getBoolean("sandbox");
		updatePaymentAccount(context, account.get("account"));
		return views.html.payment.oceanpayment.oceandetail.render(form,
				isSandbox);
	}

	private void updatePaymentAccount(PaymentContext context, String account) {
		if (OrderLableEnum.DROP_SHIPPING.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else if (OrderLableEnum.TOTAL_ORDER.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else {
			updateAccountDefault(context, account);
		}
	}

	private void updateAccountDefault(PaymentContext context, String account) {
		updateService.updatePaymentAccount(context.getOrder().getOrder()
				.getIid(), account);
	}

	private void updateAccountTotal(PaymentContext context, String account) {
		List<OrderDetail> details = context.getOrder().getDetails();
		for (OrderDetail detail : details) {
			updateService.updatePaymentAccount(detail.getIorderid(), account);
		}
	}

	private LinkedHashMap<String, String> parseToForm(Order order,
			LinkedHashMap<String, String> account, Map<String, String> map,
			List<OrderDetail> list, String backUrl) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();
		if (StringUtils.isEmpty(backUrl)) {
			backUrl = controllers.payment.routes.OceanPayment.userPOST()
					.absoluteURL(Context.current().request());
		}
		Logger.debug("Back URL: {}", backUrl);
//		String noticeUrl = controllers.payment.routes.OceanPayment.serverPOST()
//				.absoluteURL(Context.current().request());
		String noticeUrl = Play.application().configuration().getString("ipn.ocean");
		if(noticeUrl == null || noticeUrl.length() == 0){
			throw new NullPointerException("config:ipn.ocean not find");
		}
		Logger.debug("Notice URL: {}", noticeUrl);
		Map<String, String> productMap = paymentService
				.getOceanProductMap(list);

		form.put("account", account.get("account"));
		form.put("terminal", account.get("terminal"));
		form.put("order_number", order.getCordernumber());
		form.put("order_currency", order.getCcurrency());
		form.put("order_amount", String.valueOf(new DoubleCalculateUtils(order
				.getFgrandtotal()).doubleValue()));

		form.put("methods", "Ebanx");

		form.put("backUrl", backUrl);
		form.put("noticeUrl", noticeUrl);

		form.put("billing_firstName", order.getCfirstname());
		form.put("billing_lastName", order.getClastname());
		form.put("billing_email", order.getCemail());
		form.put("billing_phone", order.getCtelephone());
		form.put("billing_country", order.getCcountrysn());
		form.put("billing_state", order.getCprovince());
		form.put("billing_city", order.getCcity());
		form.put("billing_address", order.getCstreetaddress());
		form.put("billing_zip", order.getCpostalcode());

		form.put("ship_firstName", order.getCfirstname());
		form.put("ship_lastName", order.getClastname());
		form.put("ship_phone", order.getCtelephone());
		form.put("ship_country", order.getCcountrysn());
		form.put("ship_state", order.getCprovince());
		form.put("ship_city", order.getCcity());
		form.put("ship_addr", order.getCstreetaddress());
		form.put("ship_zip", order.getCpostalcode());

		form.put("productSku", productMap.get("productSku"));
		form.put("productName", productMap.get("productName"));
		form.put("productNum", productMap.get("productNum"));

		form.put("cart_api", "V1.6.0");

		form.put(
				"signValue",
				paymentService.getOceanPostSignValue(form,
						account.get("secureCode")));
		form.put("pay_userName", map.get("userName"));
		form.put("pay_email", map.get("userEmail"));
		form.put("pay_typeCode", map.get("pay_typeCode"));
		form.put("pay_cpf", map.get("pay_cpf"));
		return form;
	}

	@Override
	public boolean isNeedExtraInfo() {
		return true;
	}

	@Override
	public boolean validForm(DynamicForm df) {
		boolean b = true;
		if (StringUtils.isEmpty(df.get("userName"))) {
			df.reject("userName", "is null");
			b = false;
		}
		if (StringUtils.isEmpty(df.get("userEmail"))) {
			df.reject("userEmail", "is null");
			b = false;
		}
		if (StringUtils.isEmpty(df.get("pay_typeCode"))) {
			df.reject("pay_typeCode", "is null");
			b = false;
		}
		if (StringUtils.isEmpty(df.get("pay_cpf"))
				&& "directboleto".equals(df.get("pay_typeCode"))) {
			df.reject("pay_cpf", "is null");
			b = false;
		}
		return b;
	}

	@Override
	public String area() {
		return "BR";
	}
}
