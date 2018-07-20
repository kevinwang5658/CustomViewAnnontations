# CustomViewAnnontations

Generate a custom Factory2 class to speed inflate custom views without defaulting to reflection.

## How to Get It

project build.gradle
```Gradle

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```

module build.gradle
```Gradle

dependencies {
    implementation 'com.github.kevinwang5658.CustomViewAnnontations:cva-annotations:1.0'
}

```

## How to Use

CustomView.java
```Java

//Annotate custom view classes with @CustomView
@CustomView
public class CustomView extends View

```

Activity.java
```Java

// Set the custom factory before setContentView within your activity.
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inflater = LayoutInflater.from(this).cloneInContext(this);
    inflater.setFactory2(new CustomInflaterFactory());
    setContentView(R.layout.activity_main);
}

@NonNull
@Override
public LayoutInflater getLayoutInflater() {
    return inflater;
}

// You only need this if you get LayoutInflater using getSystemService(LAYOUT_INFLATER_SERVICE)
@Override
public Object getSystemService(String name) {
    if (name.equals(LAYOUT_INFLATER_SERVICE)) {
        if (inflater== null) {
            inflater = (LayoutInflater) super.getSystemService(name);
        }
        return inflater;
    }
    return super.getSystemService(name);
}

```
And that's all!

## License
```
Copyright 2018 Kevin Wang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
