#pragma once

namespace engine
{
	class GameObject;

	class Component
	{
	protected:
		GameObject* owner = nullptr;

	private:
		static size_t nextId;

	public:
		virtual ~Component() = default;
		virtual void update(float deltaTime) = 0;
		virtual size_t getTypeId() const = 0;

		GameObject* getOwner();

		template<typename T>
		static size_t staticTypeId()
		{
			static size_t typeId = nextId++;
			return typeId;
		}

	private:
		friend class GameObject;
	};

#define COMPONENT_TYPE_ID_IMPL(className) \
public: \
	static size_t typeId() { return Component::staticTypeId<className>(); } \
	size_t getTypeId() const override { return typeId(); }
}