#include "io/FileSystem.h"

#include <string>

#if defined (_WIN32)
#include <Windows.h>
#elif defined (__APPLE__)
#include <mach-o/dyld.h>
#elif defined (__linux__)
#include <unistd.h>
#include <limits.h>
#endif

#include "config.h"

namespace engine
{
	std::filesystem::path FileSystem::getExecutableDir() const
	{
#if defined (_WIN32)
		wchar_t buff[MAX_PATH];
		GetModuleFileNameW(NULL, buff, MAX_PATH);
		return std::filesystem::path(buff).remove_filename();
#elif defined (__APPLE__)
		uint32_t size = 0;
		_NSGetExecutablePath(nullptr, &size);
		std::string tmp(size, '\0');
		_NSGetExecutablePath(tmp.data(), &size);
		return std::filesystem::weakly_canonical(std::filesystem::path(tmp)).remove_filename();
#elif defined (__linux__)
		return std::filesystem::weakly_canonical(std::filesystem::read_symlink("/proc/self/exe")).remove_filename();
#else
		return std::filesystem::current_path();
#endif
	}

	std::filesystem::path FileSystem::getAssetsDir() const
	{
#if defined (ASSETS_ROOT)
		auto path = std::filesystem::path(std::string(ASSETS_ROOT));
		if (std::filesystem::exists(path)) {
			return path;
		}
#endif
		return std::filesystem::weakly_canonical(getExecutableDir() / "assets");
	}
}