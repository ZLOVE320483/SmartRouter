package com.zlove.smart.router.gradle

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AssetsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        AppExtension appExtension = project.extensions.getByType(AppExtension)
        Transform transform = new AssetsTransform(project)
        appExtension.registerTransform(transform)
    }
}