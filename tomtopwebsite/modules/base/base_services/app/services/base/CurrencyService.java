package services.base;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mapper.base.CurrencyMapper;
import mapper.base.CurrencyRateMapper;

import org.mybatis.guice.transactional.Transactional;

import services.ICurrencyService;
import dto.Currency;
import dto.CurrencyRate;
import extensions.InjectorInstance;

@Singleton
public class CurrencyService implements ICurrencyService {

	@Inject
	CurrencyMapper ccyMapper;

	@Inject
	CurrencyRateMapper ccyRateMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getRate(java.lang.String)
	 */
	@Override
	@CacheResult("currency")
	public double getRate(String ccy) {
		CurrencyRate rate = ccyRateMapper.findLatestRateByCode(ccy, new Date());
		if (rate == null) {
			Currency currency = ccyMapper.findByCode(ccy);
			if (currency == null) {
				throw new RuntimeException("Currency Unavailable");
			}
			return currency.getFexchangerate();
		}
		return rate.getFexchangerate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(double, java.lang.String)
	 */
	@Override
	public double exchange(double moneyInUSD, String targetCCY) {
		return moneyInUSD * getRate(targetCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(double, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public double exchange(double money, String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return money;
		}
		return money * exchange(originalCCY, targetCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@CacheResult("currency")
	public double exchange(String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return 1;
		}
		return getRate(targetCCY) / getRate(originalCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getAllCurrencies()
	 */
	@Override
	@CacheResult("currency")
	public List<Currency> getAllCurrencies() {
		return ccyMapper.getAllShowCurrencies();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyById(java.lang.Integer)
	 */
	@Override
	@CacheResult("currency")
	public Currency getCurrencyById(Integer currencyId) {
		return ccyMapper.getCurrencyById(currencyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCode(java.lang.String)
	 */
	@Override
	@CacheResult("currency")
	public Currency getCurrencyByCode(String currency) {
		return ccyMapper.findByCode(currency);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCode(java.lang.String)
	 */
	@CacheResult("currency")
	public Currency getShowCurrencyByCode(String currency, boolean bshow) {
		return ccyMapper.findShowByCode(currency, bshow);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCodes(java.util.List)
	 */
	@Override
	public List<Currency> getCurrencyByCodes(List<String> currency) {
		return ccyMapper.findByCodes(currency);
	};

	public static ICurrencyService getInstance() {
		return InjectorInstance.getInstance(CurrencyService.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#insertCurrencyRate(dto.CurrencyRate)
	 */
	@Override
	public void insertCurrencyRate(CurrencyRate currencyRate) {
		ccyRateMapper.Insert(currencyRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findLatestRate()
	 */
	@Override
	public List<CurrencyRate> findLatestRate() {
		return ccyRateMapper.findLatestRate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findUsedRate()
	 */
	@Override
	public List<CurrencyRate> findUsedRate() {
		return ccyRateMapper.findUsedRate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#useLatestRate(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional
	public boolean useLatestRate(String code, Integer id) {
		CurrencyRate currencyRate = ccyRateMapper.getLatestRateByCode(code);
		if (null == currencyRate || !currencyRate.getIid().equals(id)) {
			return false;
		}
		ccyRateMapper.banRateByCode(code);
		ccyRateMapper.useLatestRateByCode(id);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#updateRate(java.lang.Double,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	@Transactional
	public boolean updateRate(Double rate, Integer id, String code) {
		CurrencyRate currencyRate = ccyRateMapper.getLatestRateByCode(code);
		if (null == currencyRate || !currencyRate.getIid().equals(id)) {
			return false;
		}
		int i = ccyRateMapper.updateRate(rate, id);
		if (i == 1) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findRateById(java.lang.Integer)
	 */
	@Override
	public CurrencyRate findRateById(Integer id) {
		return ccyRateMapper.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getMaxCurrency(java.lang.Integer)
	 */
	@Override
	public List<Currency> getMaxCurrency(int mid) {
		return ccyMapper.getMaxCurrency(mid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyMaxId(java.lang.Integer)
	 */
	@Override
	public int getCurrencyMaxId() {
		return ccyMapper.getCurrencyByMaxId();
	}

	/**
	 * 新增货币汇率的信息
	 * 
	 * @param currencyRate
	 * @return
	 */
	public boolean addCurrencyRate(CurrencyRate currencyRate) {
		return ccyRateMapper.Insert(currencyRate) > 0 ? true : false;
	};

	/**
	 * 添加货币的数据信息
	 * 
	 * @param currency
	 * @return
	 */
	public boolean addCurrency(Currency currency) {
		return ccyMapper.Insert(currency) > 0 ? true : false;
	};
}
