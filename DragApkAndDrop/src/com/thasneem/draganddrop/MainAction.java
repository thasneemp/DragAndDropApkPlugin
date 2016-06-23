package com.thasneem.draganddrop;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

/**
 * Created by muhammed on 6/20/2016.
 */
public abstract class MainAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project data = anActionEvent.getData(PlatformDataKeys.PROJECT);
        actionPerformed(anActionEvent, data);
        
    }

    protected abstract void actionPerformed(AnActionEvent anActionEvent, Project data);
}
