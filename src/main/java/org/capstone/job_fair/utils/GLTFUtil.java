package org.capstone.job_fair.utils;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import org.capstone.job_fair.constants.MessageConstant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class GLTFUtil {
    public static GltfModel parseAndValidateModel(MultipartFile file) throws IOException {
        //the current GLTF library do not throw Exception when having parse error
        //but rather it just log the Error to logger
        //this lead to an ad hoc way is to check all model if the sum is 0 then we know the file is not valid GLB file
        GltfModelReader reader = new GltfModelReader();
        GltfModel model = reader.readWithoutReferences(file.getInputStream());

        int result = model.getTextureModels().size() +
                model.getMaterialModels().size() +
                model.getSceneModels().size() +
                model.getNodeModels().size() +
                model.getCameraModels().size() +
                model.getBufferModels().size() +
                model.getSceneModels().size() +
                model.getAnimationModels().size() +
                model.getAccessorModels().size() +
                model.getImageModels().size() +
                model.getBufferViewModels().size();
        if (result == 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.INVALID_GLB_FILE));
        }
        return model;
    }
}
