# Android Transform API Demo

This is sample code for my talk "(Android) Transformers - bytecode in disguise!".

There are three distinct modules here.

1. `transformer-libs/transformer-plugin` is a Gradle plugin that uses the
   Android Gradle Transform API to perform bytecode manipulation.
1. `transformer-libs/transformer-module` is an Android library project.
1. `tranformer-app` is an Android app that has the plugin applied to it,
   and also uses the library project.

The first two modules can be installed in a local Maven repo with:

```bash
cd transformer-libs
./gradlew install
```

After these are installed, they can participate in the build of the Android
app.

When launched, you will observe in logcat that the app is printing more
log lines than you see in the code, and the lines of code are coming
from the library project as part of build time instrumentation.

## License

The code in this project is licensed under the Apache License 2.0.

```text
Copyright 2019 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Disclaimer

This is not an officially supported Google product.
