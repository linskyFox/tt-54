package extensions.loyalty.campaign.photo;

import java.util.List;

import javax.inject.Inject;

import services.campaign.ActionRules;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.ICampaignInstance;
import services.campaign.MultiRules.Match;
import valueobjects.member.MemberIdentification;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import events.loyalty.PhotoEvent;
import events.loyalty.ReviewEvent;
import events.loyalty.VideoEvent;
import events.member.LoginEvent;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.action.point.GrantPointActionParameter;
import extensions.loyalty.campaign.rule.firstloginperday.FirstLoginPerDayActionRule;
import extensions.loyalty.campaign.rule.review.ReviewActionRule;

public class PhotoCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Override
	public String getId() {
		return "product-photo";
	}

	@Override
	public Class<?> getPayloadClass() {
		return PhotoEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		PhotoEvent event = (PhotoEvent) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		MemberIdentification id = new MemberIdentification(event.getEmail(),
				event.getSiteID());
		ctx.setActionOn(id);
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new PhotoCampaignInstance();
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		return Lists.newArrayList(fixInstance());
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		return Optional.of(fixInstance());
	}

	protected List<String> getPossibleActionRuleIDs() {
		return Lists.newArrayList(ReviewActionRule.ID);
	}

	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(GrantPointAction.ID);
	}

	private PhotoCampaignInstance fixInstance() {
		PhotoCampaignInstance i = new PhotoCampaignInstance();
		i.setInstanceId("photo-product-points");
		i.setCampaign(this);
		i.setActions(getPossibleActions());
		GrantPointActionParameter p = new GrantPointActionParameter();
		p.setSource("photo");
		p.setPoints(20);
		p.setStatus(0);
		i.setActionParams(Lists.newArrayList(p));
		i.setActionRules(new ActionRules(getPossibleActionRules(),
				Match.MATCH_ALL));
		return i;
	}
}
