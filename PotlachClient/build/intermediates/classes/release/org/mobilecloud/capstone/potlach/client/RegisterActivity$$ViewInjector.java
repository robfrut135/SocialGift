// Generated code from Butter Knife. Do not modify!
package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterActivity$$ViewInjector {
  public static void inject(Finder finder, final org.mobilecloud.capstone.potlach.client.RegisterActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296285, "field 'name_'");
    target.name_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296281, "field 'userName_'");
    target.userName_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296282, "field 'password_'");
    target.password_ = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296287, "field 'questionFrecuency_'");
    target.questionFrecuency_ = (android.widget.RadioGroup) view;
    view = finder.findRequiredView(source, 2131296291, "field 'viewInappropiated_'");
    target.viewInappropiated_ = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131296293, "field 'questionOrderBy_'");
    target.questionOrderBy_ = (android.widget.RadioGroup) view;
    view = finder.findRequiredView(source, 2131296283, "method 'register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
  }

  public static void reset(org.mobilecloud.capstone.potlach.client.RegisterActivity target) {
    target.name_ = null;
    target.userName_ = null;
    target.password_ = null;
    target.questionFrecuency_ = null;
    target.viewInappropiated_ = null;
    target.questionOrderBy_ = null;
  }
}
