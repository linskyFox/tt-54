package extensions.activity;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IPrizeExtension extends IExtensionPoint {

	/**
	 * Implementors need to register their own concrete
	 * ITemplateFragmentProvider to the Multibinder provided.
	 * 
	 * @param binder
	 */
	void registerActivityPrizeProviders(Multibinder<IActivityPrizeProvider> binder); 
}
