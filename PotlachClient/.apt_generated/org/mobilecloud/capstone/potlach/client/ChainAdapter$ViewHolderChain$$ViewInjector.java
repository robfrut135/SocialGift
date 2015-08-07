// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ChainAdapter$ViewHolderChain$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.ChainAdapter.ViewHolderChain target, Object source) {
    org.mobilecloud.capstone.potlach.client.ViewHolder$$ViewInjector.inject(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131296258, "method 'giftFullScreen'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.giftFullScreen();
        }
      });
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.ChainAdapter.ViewHolderChain target) {
    org.mobilecloud.capstone.potlach.client.ViewHolder$$ViewInjector.reset(target);

  }
}
