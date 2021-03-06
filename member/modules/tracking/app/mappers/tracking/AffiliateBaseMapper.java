package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateBase;

public interface AffiliateBaseMapper {
    int insert(AffiliateBase record);

    int insertSelective(AffiliateBase record);
    
    int updateByPrimaryKey(AffiliateBase record);

    int deleteByAid(String aid);

    @Select("select * from t_affiliate_base where caid=#{0}")
    List<AffiliateBase> getAffiliateBaseByAId(String aid);
    
    @Select("select * from t_affiliate_base where caid=#{0} limit 1")
    AffiliateBase getByAId(String aid);
    
    @Select("select * from t_affiliate_base")
    List<AffiliateBase> getAffiliateBases();

}