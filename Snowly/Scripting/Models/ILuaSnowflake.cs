using JetBrains.Annotations;

namespace Snowly.Scripting.Models;

[UsedImplicitly(ImplicitUseTargetFlags.WithInheritors | ImplicitUseTargetFlags.WithMembers)]
public interface ILuaSnowflake : ILuaModel
{
    ulong ID { get; }
}
