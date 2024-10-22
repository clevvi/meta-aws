SUMMARY = "Amazon Kinesis Video Streams CPP Producer"
DESCRIPTION = "Amazon Kinesis Video Streams Producer SDK for C/C++ makes it easy to build an on-device application that securely connects to a video stream, and reliably publishes video and other media data to Kinesis Video Streams. It takes care of all the underlying tasks required to package the frames and fragments generated by the device's media pipeline. The SDK also handles stream creation, token rotation for secure and uninterrupted streaming, processing acknowledgements returned by Kinesis Video Streams, and other tasks."
HOMEPAGE = "https://github.com/awslabs/amazon-kinesis-video-streams-producer-sdk-cpp"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

DEPENDS += "\
    amazon-kvs-producer-sdk-c \
    log4cplus \
    openssl \
    "

PROVIDES += "aws/amazon-kvs-producer-sdk-cpp"

BRANCH ?= "master"
# nooelint: oelint.file.patchsignedoff
SRC_URI = "git://github.com/awslabs/amazon-kinesis-video-streams-producer-sdk-cpp.git;protocol=https;branch=${BRANCH} \
           file://amazon-kvs-producer-sdk-cpp-deps.patch \
           file://global-thread-sleep-linkerror.patch \
           "

SRCREV = "7acf272672ced72bbbad85fe39d366deca57280e"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

PACKAGECONFIG ??= "gstreamer"

PACKAGECONFIG[gstreamer] = "-DBUILD_GSTREAMER_PLUGIN=ON,-DBUILD_GSTREAMER_PLUGIN=OFF,curl log4cplus openssl gstreamer1.0 gstreamer1.0-plugins-base"

# Notify that libraries are not versioned
FILES_SOLIBSDEV = ""

FILES:${PN} += "\
    ${libdir}/pkgconfig/*.pc \
    ${libdir}/*.so \
    ${libdir}/*/*.so \
    "

FILES:${PN}-dev += "\
    ${includedir}/* \
    "

CFLAGS:append = " -Wl,-Bsymbolic"

EXTRA_OECMAKE += "\
    -DBUILD_DEPENDENCIES=OFF \
    -DBUILD_JNI=OFF \
    -DBUILD_STATIC=OFF \
    -DBUILD_DEPENDENCIES=OFF \
    -DBUILD_OPENSSL_PLATFORM=OFF \
    -DCODE_COVERAGE=OFF \
    -DCOMPILER_WARNINGS=OFF \
    -DADDRESS_SANITIZER=OFF \
    -DMEMORY_SANITIZER=OFF \
    -DTHREAD_SANITIZER=OFF \
    -DUNDEFINED_BEHAVIOR_SANITIZER=OFF \
    -DBUILD_TEST=OFF \
    -DBUILD_SHARED_LIBS=ON \
    -DCMAKE_BUILD_TYPE=Release \
    "

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/com
    install -d ${D}${includedir}/com/amazonaws
    install -d ${D}${includedir}/com/amazonaws/kinesis
    install -d ${D}${includedir}/com/amazonaws/kinesis/video
    install -d ${D}${includedir}/com/amazonaws/kinesis/video/producer
    install -d ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni
    install -d ${D}${includedir}/com/amazonaws/kinesis/video/producer/credential-providers
    install -d ${D}${includedir}/com/amazonaws/kinesis/video/producer/gstreamer

    install -m 0640 ${WORKDIR}/git/src/CachingEndpointOnlyCallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/CachingEndpointOnlyCallbackProvider.h
    install -m 0640 ${WORKDIR}/git/src/ThreadSafeMap.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/ThreadSafeMap.h
    install -m 0640 ${WORKDIR}/git/src/DefaultCallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/DefaultCallbackProvider.h
    install -m 0640 ${WORKDIR}/git/src/StreamCallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/StreamCallbackProvider.h
    install -m 0640 ${WORKDIR}/git/src/KinesisVideoProducer.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/KinesisVideoProducer.h
    install -m 0640 ${WORKDIR}/git/src/DefaultDeviceInfoProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/DefaultDeviceInfoProvider.h
    install -m 0640 ${WORKDIR}/git/src/CallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/CallbackProvider.h
    install -m 0640 ${WORKDIR}/git/src/StreamTags.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/StreamTags.h
    install -m 0640 ${WORKDIR}/git/src/Logger.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/Logger.h
    install -m 0640 ${WORKDIR}/git/src/KinesisVideoStream.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/KinesisVideoStream.h
    install -m 0640 ${WORKDIR}/git/src/ClientCallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/ClientCallbackProvider.h

    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/KinesisVideoClientWrapper.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/KinesisVideoClientWrapper.h
    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/SyncMutex.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/SyncMutex.h
    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/JNICommon.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/JNICommon.h
    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/Parameters.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/Parameters.h
    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/com_amazonaws_kinesisvideo_internal_producer_jni_NativeKinesisVideoProducerJni.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/com_amazonaws_kinesisvideo_internal_producer_jni_NativeKinesisVideoProducerJni.h
    install -m 0640 ${WORKDIR}/git/src/JNI/include/com/amazonaws/kinesis/video/producer/jni/TimedSemaphore.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/jni/TimedSemaphore.h

    install -m 0640 ${WORKDIR}/git/src/credential-providers/RotatingCredentialProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/credential-providers/RotatingCredentialProvider.h
    install -m 0640 ${WORKDIR}/git/src/credential-providers/IotCertCredentialProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/credential-providers/IotCertCredentialProvider.h

    install -m 0640 ${WORKDIR}/git/src/gstreamer/KvsSinkStreamCallbackProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/KvsSinkStreamCallbackProvider.h
    install -m 0640 ${WORKDIR}/git/src/gstreamer/KvsSinkDeviceInfoProvider.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/credential-providers/KvsSinkDeviceInfoProvider.h
    install -m 0640 ${WORKDIR}/git/src/gstreamer/gstkvssink.h ${D}${includedir}/com/amazonaws/kinesis/video/producer/credential-providers/gstkvssink.h

    install -m 0755 ${B}/libKinesisVideoProducer.so ${D}${libdir}/

    if ${@bb.utils.contains('PACKAGECONFIG', 'gstreamer', 'true', 'false', d)}; then
        install -d ${D}${libdir}/gstreamer-1.0
        install -m 755 ${B}/libgstkvssink.so ${D}${libdir}/gstreamer-1.0/libgstkvssink.so
    fi
}

BBCLASSEXTEND = "native nativesdk"