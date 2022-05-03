package com.zlove.smart.router.gradle

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.api.ApkVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project


class AssetsTransform extends Transform {

    private Project project;

    AssetsTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "AssetsTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        println("==== AssetsTransform ====")
        generateMockJson()
    }

    private def generateMockJson() {
        println "开始生成 abmock_model.txt."
        project.android.applicationVariants.all { ApkVariant variant ->
            variant.outputs.each { BaseVariantOutput output ->
                def outputFile = variant.mergeAssetsProvider.get().outputDir.get().asFile
                if (outputFile.exists()) {
                    def configFile = new File(
                            "${outputFile.absolutePath}/abmock_model.txt")
                    PrintWriter writer = new PrintWriter(configFile)
                    writer.print("zlove.zhang")
                    writer.close()
                }
            }
        }

    }
}