// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GiftActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.GiftActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296260, "field 'title_'");
    target.title_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296261, "field 'description_'");
    target.description_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296262, "field 'image_' and method 'selectImage'");
    target.image_ = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectImage();
        }
      });
    view = finder.findRequiredView(source, 2131296263, "method 'saveGift'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveGift();
        }
      });
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.GiftActivity target) {
    target.title_ = null;
    target.description_ = null;
    target.image_ = null;
  }
}
