// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginScreenActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.LoginScreenActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296282, "field 'password_'");
    target.password_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296281, "field 'userName_'");
    target.userName_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296284, "method 'login'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.login();
        }
      });
    view = finder.findRequiredView(source, 2131296283, "method 'newAccount'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.newAccount();
        }
      });
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.LoginScreenActivity target) {
    target.password_ = null;
    target.userName_ = null;
  }
}
