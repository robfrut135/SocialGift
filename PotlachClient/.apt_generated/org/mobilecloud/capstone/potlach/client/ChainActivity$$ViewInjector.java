// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ChainActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.ChainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296257, "field 'gridGiftsChain_'");
    target.gridGiftsChain_ = (android.widget.GridView) view;
    view = finder.findRequiredView(source, 2131296256, "method 'addGift'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addGift();
        }
      });
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.ChainActivity target) {
    target.gridGiftsChain_ = null;
  }
}
