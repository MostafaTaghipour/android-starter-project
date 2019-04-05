package ir.rainyday.android.starter.modules.sample


fun SampleEntity.toDTO(): SampleDTO {
    return SampleDTO(id, title)
}

fun SampleEntity.toJson(): SampleJson {
    return SampleJson(id, title)
}

fun SampleDTO.toEntity(): SampleEntity {
    return SampleEntity(id, title)
}

fun SampleDTO.toJson(): SampleJson {
    return SampleJson(id, title)
}

fun SampleJson.toEntity(): SampleEntity {
    return SampleEntity(id, title)
}

fun SampleJson.toDTO(): SampleDTO {
    return SampleDTO(id, title)
}
