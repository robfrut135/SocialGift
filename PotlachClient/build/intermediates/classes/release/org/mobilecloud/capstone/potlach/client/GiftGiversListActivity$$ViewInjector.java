// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GiftGiversListActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.GiftGiversListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296280, "field 'listGivers'");
    target.listGivers = (android.widget.TableLayout) view;
    view = finder.findRequiredView(source, 2131296273, "field 'title_'");
    target.title_ = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131296275, "field 'columnKey'");
    target.columnKey = (android.widget.TextView) view;
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.GiftGiversListActivity target) {
    target.listGivers = null;
    target.title_ = null;
    target.columnKey = null;
  }
}
