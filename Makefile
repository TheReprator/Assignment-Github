# === CONFIG ===
GRADLEW=./gradlew

# clear terminal
clear:
	clear

# clear build
cleanBuild: clear
	$(GRADLEW) clean

# clear Build manually
manualClean: cleanBuild
	@echo "Clean build manually"
	rm -rf .idea
	rm -rf .gradle
	rm -rf build
	rm -rf */build
	rm -rf iosApp/iosApp.xcworkspace
	rm -rf iosApp/Pods
	rm -rf iosApp/iosApp.xcodeproj/project.xcworkspace
	rm -rf iosApp/iosApp.xcodeproj/xcuserdata
	rm -rf kotlin-js-store
	@echo "✅ Done!"

# Run unit test
taskList: cleanBuild
	@echo "Task List"
	$(GRADLEW) task
	@echo "✅ Done!"

# Generate compose stability report
reportCompose: manualClean
	@echo "Compose Compiler Report"
	$(GRADLEW) assembleDebug -PcomposeCompilerReports=true  --rerun-tasks
	@echo "✅ Done!"

# Run Android build
buildAndroid: cleanBuild
	@echo "Android build"
	$(GRADLEW) androidApp:app:installDebug
	@echo "✅ Done!"

# Run Desktop build
buildDesktop: cleanBuild
	@echo "Desktop build"
	$(GRADLEW) :desktopapp:run
	@echo "✅ Done!"

# Run web build
buildWeb: cleanBuild
	@echo "Web build"
	$(GRADLEW) webApp:js:wasmJsBrowserDevelopmentRun
	@echo "✅ Done!"

# Run All test
allTest: cleanBuild