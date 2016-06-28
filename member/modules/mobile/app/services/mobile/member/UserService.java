package services.mobile.member;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.loyalty.MemberIntegralHistoryMapper;
import mapper.member.MemberBaseMapper;
import mapper.member.MemberPhotoMapper;

import org.apache.commons.codec.binary.Hex;

import play.Logger;
import services.ICountryService;
import services.MemberEnquiryService;
import services.base.utils.FileUtils;
import services.base.utils.StringUtils;
import services.loyalty.IPointsService;
import services.loyalty.coupon.impl.CouponService;
import services.member.IMemberUpdateService;
import services.order.IOrderCountService;
import utils.DateUtils;
import utils.ValidataUtils;
import valueobjects.base.Page;
import valueobjects.order_api.OrderCount;

import com.google.common.collect.Lists;

import dao.messaging.IBroadcastDao;
import dao.messaging.IMessageDao;
import dto.Country;
import dto.member.MemberBase;
import dto.member.MemberPhoto;
import dto.mobile.CouponsInfo;
import dto.mobile.MessageDtl;
import dto.mobile.MessageList;
import dto.mobile.PointInfo;
import dto.mobile.UserInfo;
import entity.loyalty.Coupon;
import entity.loyalty.MemberIntegralHistory;
import entity.messaging.Broadcast;
import enums.messaging.Status;

//import dao.messaging.IMessageDao;

public class UserService {

	@Inject
	MemberEnquiryService memberEnquiryService;
	@Inject
	IOrderCountService orderCountService;
	@Inject
	IMessageDao messageDao;
	@Inject
	CouponService couponService;
	@Inject
	MemberBaseMapper memberBaseMapper;
	@Inject
	IPointsService pointsService;
	@Inject
	IMemberUpdateService memberUpdateService;
	@Inject
	IBroadcastDao broadcastDao;
	@Inject
	MemberIntegralHistoryMapper historyMapper;
	@Inject
	MemberPhotoMapper photoMapper;
	@Inject
	ICountryService countryService;

	private final static int isShow = 1;
	private final static boolean isNormal = true;

	/**
	 * 获取昵称
	 * 
	 * @param email
	 * @return String
	 */
	public String getNickName(String email) {
		return memberEnquiryService.getUserNameByMemberEmail(email);
	}

	/**
	 * 获取订单分类数量
	 * 
	 * @param email
	 * @return OrderCount
	 */
	public OrderCount getOrderCount(String email, int siteId) {

		return orderCountService.getCountByEmail(email, siteId, isShow,
				isNormal);
	}

	/**
	 * 获取未读取的消息总数
	 * 
	 * @param
	 * @return Integer
	 */
	public int getUnMsgCount(String email) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("userId", email);
		return this.messageDao.getMyMessageTotal(paras);

	}

	/**
	 * 获取用户个人中心信息
	 * 
	 * @param email
	 * @param email
	 * @return UserInfo
	 */
	public UserInfo getUserInfo(String email, int siteId) {
		UserInfo userInfo = new UserInfo();
		String nick = getNickName(email);
		if (null == nick) {
			Logger.debug("nick is null");
			nick = "";
		}
		Integer msgQty = getUnMsgCount(email);
		if (null == msgQty) {
			Logger.debug("msgQty is null");
			msgQty = 0;
		}
		OrderCount oc = getOrderCount(email, siteId);
		if (null == oc) {
			Logger.debug("OrderCount is null");
			oc = new OrderCount();
		}

		userInfo.setEmail(email);
		userInfo.setNick(nick);
		userInfo.setMsgqty(msgQty);
		userInfo.setCancelledqty(ValidataUtils.validataInt(oc.getCancelled()));
		userInfo.setConfirmqty(ValidataUtils.validataInt(oc.getConfirmed()));
		userInfo.setDealqty(ValidataUtils.validataInt(oc.getProcessing()));
		userInfo.setNopayqty(ValidataUtils.validataInt(oc.getPending()));
		userInfo.setDispatqty(ValidataUtils.validataInt(oc.getDispatched()));
		userInfo.setRefunqty(ValidataUtils.validataInt(oc.getRefunded()));

		return userInfo;
	}

	/**
	 * 获取未使用的优惠券信息
	 * 
	 * @param page
	 * @param pageSiz
	 * @param userEmail
	 * @return Page<CouponsInfo>
	 */
	public utils.Page<CouponsInfo> getUnusedCoupons(int page, int pageSize,
			String userEmail) {

		Page<Coupon> cuPage = couponService.selectMyCouponUnusedForPage(page,
				pageSize, userEmail);
		List<Coupon> clist = new ArrayList<Coupon>();
		if (cuPage != null) {
			clist = cuPage.getList();
			if (null == cuPage || null == clist) {
				return null;
			}
		}

		utils.Page<CouponsInfo> cInfoPage = null;
		if (clist.size() > 0) {
			List<CouponsInfo> coulist = Lists.transform(
					clist,
					c -> {
						CouponsInfo cInfo = new CouponsInfo();
						cInfo.setCode(c.getCode());
						cInfo.setFlag(c.getValueType());
						cInfo.setDescr(c.getRuleName());
						if (c.isCash()) {
							cInfo.setDis(c.getPar());
						} else {
							cInfo.setDis(c.getDiscount());
						}
						cInfo.setMinAmt(c.getMinAmount());
						String vdate = "";
						try {
							vdate = DateUtils.addOndDay(c.getCreateDateStr(),
									c.getValidDays());
						} catch (Exception e) {
							Logger.error("DateUtils.addOndDay exception"
									+ e.toString());
						}
						cInfo.setVdate(Long.parseLong(vdate));
						return cInfo;
					});

			cInfoPage = new utils.Page<CouponsInfo>(coulist,
					cuPage.totalCount(), cuPage.pageNo(), cuPage.pageSize());
		}

		return cInfoPage;
	}

	/**
	 * 获取已使用的优惠券信息
	 * 
	 * @param page
	 * @param pageSiz
	 * @param userEmail
	 * @return Page<CouponsInfo>
	 */
	public utils.Page<CouponsInfo> getUsedCoupons(int page, int pageSize,
			String userEmail) {

		Page<Coupon> cuPage = couponService.selectMyCouponUsedForPage(page,
				pageSize, userEmail);

		List<Coupon> clist = new ArrayList<Coupon>();
		if (cuPage != null) {
			clist = cuPage.getList();
			if (null == cuPage || null == clist) {
				return null;
			}
		}

		utils.Page<CouponsInfo> cInfoPage = null;
		if (clist.size() > 0) {
			List<CouponsInfo> coulist = Lists.transform(
					clist,
					c -> {
						CouponsInfo cInfo = new CouponsInfo();
						cInfo.setCode(c.getCode());
						cInfo.setFlag(c.getValueType());
						cInfo.setDescr(c.getRuleName());
						if (c.isCash()) {
							cInfo.setDis(c.getPar());
						} else {
							cInfo.setDis(c.getDiscount());
						}
						cInfo.setMinAmt(c.getMinAmount());
						String vdate = "";
						try {
							vdate = DateUtils.addOndDay(c.getCreateDateStr(),
									c.getValidDays());
						} catch (Exception e) {
							Logger.error("DateUtils.addOndDay exception"
									+ e.toString());
						}
						cInfo.setVdate(Long.parseLong(vdate));
						return cInfo;
					});

			cInfoPage = new utils.Page<CouponsInfo>(coulist,
					cuPage.totalCount(), cuPage.pageNo(), cuPage.pageSize());
		}

		return cInfoPage;
	}

	/**
	 * 获取个人资料
	 * 
	 * @param email
	 * @param siteId
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getPeronInfo(String email, int siteId) {
		String nickName = memberEnquiryService.getUserNameByMemberEmail(email);
		MemberBase mbb = memberBaseMapper.getUserByEmail(email, siteId);
		if (null == mbb) {
			return null;
		}
		Map<String, Object> strMap = new HashMap<String, Object>();
		strMap.put("nickname", ValidataUtils.validataStr(nickName));
		strMap.put("fname", ValidataUtils.validataStr(mbb.getCfirstname()));
		strMap.put("lname", ValidataUtils.validataStr(mbb.getClastname()));
		strMap.put("gender", ValidataUtils.validataInt(mbb.getIgender()));
		strMap.put("birth", ValidataUtils.ValidataDate(mbb.getDbirth()));
		strMap.put("country", ValidataUtils.validataStr(mbb.getCcountry()));
		Country co = countryService.getCountryByShortCountryName(mbb
				.getCcountry());
		if (co != null) {
			strMap.put("cname", co.getCname());
		} else {
			strMap.put("cname", "");
		}
		strMap.put("about", ValidataUtils.validataStr(mbb.getCaboutme()));
		strMap.put("tel", ValidataUtils.validataStr(""));
		return strMap;
	}

	/**
	 * 获取用户可用积分记录
	 * 
	 * @param email
	 * @param siteId
	 * @param page
	 * @param pageSize
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getPeronUnPoints(String email, int siteId,
			int page, int pageSize) {
		int dateType = 0;
		Integer totalUnuseCount = pointsService.getTotalIntegralCountByEmail(
				siteId, email);
		Integer totalPoints = pointsService.getUsefulPoints(email, siteId);
		Page<MemberIntegralHistory> mih = pointsService
				.getIntegralHistoriesByEmail(siteId, email, 1, page, pageSize,
						dateType, totalUnuseCount);
		List<PointInfo> polist = Lists.transform(mih.getList(), m -> {
			PointInfo pin = new PointInfo();
			pin.setType(m.getCdotype());
			pin.setCdate(m.getDcreatedate().getTime());
			pin.setPoint(m.getIintegral());
			// pin.setRemark(m.getCremark());

				return pin;
			});

		Map<String, Object> objMap = null;
		if (polist.size() > 0) {
			objMap = new HashMap<String, Object>();
			objMap.put("list", polist);
			objMap.put("total", mih.totalCount());
			objMap.put("p", mih.pageNo());
			objMap.put("size", mih.pageSize());
			objMap.put("pts", totalPoints);
		}

		return objMap;
	}

	/**
	 * 获取用户已用积分记录
	 * 
	 * @param email
	 * @param siteId
	 * @param page
	 * @param pageSize
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getPeronPoints(String email, int siteId,
			int page, int pageSize) {
		int dateType = 0;
		Integer totalUsedCount = pointsService.getTotalUsedCountByEmail(siteId,
				email, pointsService.LOCK_TYPE);
		Integer totalUsedPoints = pointsService.getTotalUsePointByEmail(siteId,
				email);
		Page<MemberIntegralHistory> mih = pointsService.getUsedPointsByEmail(
				siteId, email, page, pageSize, dateType, totalUsedCount);
		if (null == mih) {
			return null;
		}
		List<PointInfo> polist = Lists.transform(mih.getList(), m -> {
			PointInfo pin = new PointInfo();
			pin.setType(m.getCdotype());
			pin.setCdate(m.getDcreatedate().getTime());
			pin.setPoint(m.getIintegral());
			// pin.setRemark(m.getCremark());

				return pin;
			});

		Map<String, Object> objMap = null;
		if (polist.size() > 0) {
			objMap = new HashMap<String, Object>();
			objMap.put("list", polist);
			objMap.put("total", mih.totalCount());
			objMap.put("p", mih.pageNo());
			objMap.put("size", mih.pageSize());
			objMap.put("pts", totalUsedPoints);
		}

		return objMap;
	}

	/**
	 * 获取用户积分
	 *
	 * @param email
	 * @param siteID
	 * @param page
	 * @param pageSize
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getPoints(String email, int siteId, int page,
			int pageSize) {
		Map<String, Object> objMap = new HashMap<String, Object>();

		if (page == 1) {
			Integer totalPoints = pointsService.getUsefulPoints(email, siteId);// 获取用户所有积分
																				// -
																				// lock
			Integer locktotal = pointsService.getLockedPoints(email, siteId);// 获取锁定积分
			Integer total = totalPoints + Math.abs(locktotal);// 总积分
			objMap.put("freeze", Math.abs(locktotal));
			objMap.put("pts", total);
		}

		Integer totalCount = pointsService.getTotalIntegralCountByEmail(siteId,
				email);// 获取总记录数

		List<MemberIntegralHistory> memberIntegralHistories = historyMapper
				.getMemberIntegralHistoryList(email, siteId, null, null, 1,
						pageSize, page);

		if (null == memberIntegralHistories) {
			return null;
		}

		List<PointInfo> polist = Lists.transform(memberIntegralHistories,
				m -> {
					PointInfo pin = new PointInfo();
					pin.setType(m.getCdotype());
					pin.setCdate(m.getDcreatedate().getTime());
					pin.setPoint(m.getIintegral());
					// pin.setRemark(m.getCremark());

				return pin;
			});

		if (polist.size() > 0) {
			objMap.put("list", polist);
			objMap.put("total", totalCount);
			objMap.put("p", page);
			objMap.put("size", pageSize);
		}

		return objMap;
	}

	/**
	 * 更新个人资料
	 * 
	 * @param email
	 * @param siteId
	 * @param nickName
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param birth
	 * @param country
	 * @param about
	 * @return boolean
	 */
	public boolean update(String email, int siteId, String nickName,
			String firstName, String lastName, int gender, Date birth,
			String country, String about) {
		MemberBase mbb = memberBaseMapper.getUserByEmail(email, siteId);
		mbb.setCaccount(nickName);
		mbb.setCfirstname(firstName);
		mbb.setClastname(lastName);
		mbb.setIgender(gender);
		mbb.setDbirth(birth);
		mbb.setCcountry(country);
		mbb.setCaboutme(about);

		return memberUpdateService.updateMember(mbb);

	}

	/**
	 * 获取消息列表
	 * 
	 * @param
	 * @return Integer
	 */
	public HashMap<String, Object> getMyMessageList(String email, int page,
			int pageSize) {

		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userId", email);

		List<Broadcast> messlist = this.messageDao.getMyMessageForPage(paras);
		int total = this.getUnMsgCount(email);
		if (null == messlist) {
			return null;
		}
		List<MessageList> mlist = Lists.transform(messlist, c -> {
			MessageList ml = new MessageList();
			ml.setId(c.getId());
			ml.setContent(c.getContent());
			ml.setCreateDate(c.getCreateDate().getTime());
			ml.setFrom(c.getFrom());
			ml.setStatus(c.getStatus());
			ml.setSubject(c.getSubject());
			ml.setTable(c.getTable());
			return ml;
		});
		if (mlist.size() == 0) {
			return null;
		}
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("list", mlist);
		objMap.put("total", total);
		objMap.put("p", page);
		objMap.put("size", pageSize);

		return objMap;

	}

	/**
	 * 获取消息详情并处理阅读状态
	 * 
	 * @param email
	 * @param mid
	 * @param table
	 * @return Integer
	 */
	public HashMap<String, Object> getMessageDtl(String email, String mid,
			String table) {
		Broadcast result = null;
		if ("b".equals(table)) {
			boolean isExisted = this.isExistedByBroadcastId(email, mid);
			if (!isExisted) {
				// 把消息设置为已阅
				this.readMyBroadcastMessage(email, mid);
			}
			// 获取详情
			result = this.getBsDetail(mid);
		} else if ("i".equals(table)) {
			this.readMessage(mid);
			result = this.getDetail(mid);
		}
		if (null == result) {
			return null;
		}

		MessageDtl mDtl = new MessageDtl();
		mDtl.setId(mid);
		mDtl.setFrom(result.getFrom());
		mDtl.setContent(result.getContent());
		mDtl.setCreateDate(result.getCreateDate().getTime());
		mDtl.setSendMethod(result.getSendMethod());
		mDtl.setStatus(result.getStatus());
		mDtl.setSubject(result.getSubject());
		mDtl.setType(result.getTypeEnum().getDescribeEN());
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("info", mDtl);

		return objMap;
	}

	/**
	 * 判断消息是否已阅
	 * 
	 * @param email
	 * @param broadcastId
	 * @return boolean
	 */
	public boolean isExistedByBroadcastId(String email, String broadcastId) {

		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("userId", email);
		paras.put("broadcastId", broadcastId);
		return this.messageDao.isExisted(paras);
	}

	/**
	 * 设置系统消息为已阅
	 * 
	 * @param email
	 * @param broadcastId
	 * @return Integer
	 */
	public int readMyBroadcastMessage(String email, String broadcastId) {

		if (StringUtils.isEmpty(email)) {
			return 0;
		}
		int status = Status.READ.getCode();

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("broadcastId", broadcastId);
		paras.put("userId", email);
		paras.put("status", status);
		return this.broadcastDao.markMyBroadcastMessage(paras);
	}

	/**
	 * 设置人工消息为已阅
	 * 
	 * @param email
	 * @param broadcastId
	 * @return Integer
	 */
	public int readMessage(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("id", id);
		paras.put("status", Status.READ.getCode());
		return this.messageDao.updateMessageStatus(paras);
	}

	/**
	 * 获取系统消息详情
	 * 
	 * @param broadcastId
	 * @return Broadcast
	 */
	public Broadcast getBsDetail(String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", broadcastId);
		return this.broadcastDao.getDetail(paras);
	}

	/**
	 * 获取人工消息详情
	 * 
	 * @param broadcastId
	 * @return Broadcast
	 */
	public Broadcast getDetail(String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", broadcastId);
		return this.messageDao.getDetail(paras);
	}

	/**
	 * 上传用户头像
	 * 
	 * @return boolean
	 */
	public boolean updatePhoto(String email, Integer siteId,
			String contenttype, File file) {

		if (file == null) {
			return false;
		}

		byte[] buff = FileUtils.toByteArray(file);
		if (buff == null) {
			return false;
		}
		MemberPhoto photo = null;
		photo = photoMapper.getMemberPhotoByEamil(email, siteId);

		int result = 0;
		String md5;
		try {
			md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(
					buff));
		} catch (NoSuchAlgorithmException e) {
			Logger.error("member upload photo error:{}", e.getMessage());
			return false;
		}
		if (photo == null) {
			photo = new MemberPhoto();
			photo.setBfile(buff);
			photo.setCcontenttype(contenttype);
			photo.setCemail(email);
			photo.setCmd5(md5);
			photo.setIwebsiteid(siteId);
			result = photoMapper.insert(photo);
		} else {
			photo.setBfile(buff);
			photo.setCmd5(md5);
			photo.setCcontenttype(contenttype);
			result = photoMapper.updateByPrimaryKey(photo);
		}
		return result > 0 ? true : false;
	}

}
