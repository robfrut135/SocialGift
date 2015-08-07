// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GiftListAdapter$ViewHolderListGift$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.GiftListAdapter.ViewHolderListGift target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296267, "field 'description_'");
    target.description_ = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131296270, "field 'counterTouched_' and method 'likeGift'");
    target.counterTouched_ = (android.widget.CheckBox) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.likeGift();
        }
      });
    view = finder.findRequiredView(source, 2131296268, "field 'deleteGift_' and method 'deleteGift'");
    target.deleteGift_ = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteGift();
        }
      });
    view = finder.findRequiredView(source, 2131296271, "field 'inappropiatedCheck_' and method 'innapropiatedGift'");
    target.inappropiatedCheck_ = (android.widget.CheckBox) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.innapropiatedGift();
        }
      });
    view = finder.findRequiredView(source, 2131296272, "field 'chainGifts_' and method 'chainGift'");
    target.chainGifts_ = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.chainGift();
        }
      });
    view = finder.findRequiredView(source, 2131296266, "field 'title_'");
    target.title_ = (android.widget.TextView) view;
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

  public static void reset(org.mobilecloud.capstone.potlach.client.GiftListAdapter.ViewHolderListGift target) {
    target.description_ = null;
    target.counterTouched_ = null;
    target.deleteGift_ = null;
    target.inappropiatedCheck_ = null;
    target.chainGifts_ = null;
    target.title_ = null;
  }
}
