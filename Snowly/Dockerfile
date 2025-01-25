FROM mcr.microsoft.com/dotnet/sdk:8.0-alpine AS build
COPY . /src
WORKDIR /src
ARG BUILD_CONFIGURATION=Release
RUN dotnet publish Snowly -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

FROM mcr.microsoft.com/dotnet/runtime:8.0-alpine AS final
WORKDIR /app
COPY --from=build /app/publish .
ENTRYPOINT ["dotnet", "Snowly.dll"]