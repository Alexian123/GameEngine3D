#pragma once

#include <filesystem>

namespace engine
{
	class FileSystem
	{
	public:
		std::filesystem::path getExecutableDir() const;
		std::filesystem::path getAssetsDir() const;
	};
}