package com.github.dankinsoid.appcodeassets.models

import com.google.gson.annotations.SerializedName

enum class CompressionType {
    automatic,
    lossless,
    lossy,
    @SerializedName("gpu-optimized-best") gpuOptimizedBest,
    @SerializedName("gpu-optimized-smallest") gpuOptimizedSmallest;

    fun title() = when (this) {
        automatic -> "Automatic"
        lossless -> "Lossless"
        lossy -> "Lossy"
        gpuOptimizedBest -> "GPU Best Quality"
        gpuOptimizedSmallest -> "GPU Smallest Size"
    }
}
