/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "vendor.dolby.media.c2@1.0-service"

#include <binder/ProcessState.h>
#include <codec2/hidl/1.0/ComponentStore.h>
#include <hidl/HidlTransportSupport.h>
#include <log/log.h>

#include <csignal>
#include <memory>

namespace android {
std::shared_ptr<C2ComponentStore> GetCodec2DolbyComponentStore();
}

int main() {
    ALOGI("Dolby Codec2 service starting");
    std::signal(SIGPIPE, SIG_IGN);
    android::ProcessState::initWithDriver("/dev/vndbinder");
    android::ProcessState::self()->startThreadPool();
    android::hardware::configureRpcThreadpool(8, true /* callerWillJoin */);

    const auto backend = android::GetCodec2DolbyComponentStore();
    if (!backend) {
        ALOGE("Cannot create Dolby Codec2 component store");
        return 1;
    }

    using android::hardware::media::c2::V1_0::utils::ComponentStore;
    // Allocate with the same class layout as the source-built HIDL helper.
    const auto store = android::sp<ComponentStore>::make(backend);
    const android::status_t status = store->registerAsService("default1");
    if (status != android::OK) {
        ALOGE("Cannot register Dolby Codec2 component store: %d", status);
        return 1;
    }

    ALOGI("Dolby Codec2 component store registered as default1");
    android::hardware::joinRpcThreadpool();
    return 0;
}
