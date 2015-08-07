// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GiftListActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.GiftListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296265, "field 'searchText_'");
    target.searchText_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296264, "field 'giftList_'");
    target.giftList_ = (android.widget.ListView) view;
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

  public static void reset(org.mobilecloud.capstone.potlach.client.GiftListActivity target) {
    target.searchText_ = null;
    target.giftList_ = null;
  }
}
