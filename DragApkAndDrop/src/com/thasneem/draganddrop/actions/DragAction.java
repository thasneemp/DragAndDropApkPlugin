package com.thasneem.draganddrop.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.thasneem.draganddrop.MainAction;
import com.thasneem.draganddrop.actions.views.DaggerClass;

/**
 * Created by muhammed on 6/20/2016.
 */
public class DragAction extends MainAction {
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Project data) {

        DaggerClass daggerClass = new DaggerClass(data);
        daggerClass.pack();
        daggerClass.setVisible(true);
    }
}
