@REM. this path doesn't seem to work with local path or localhost!
@ECHO Enter a path of the remotely hosted swagger specification JSON file:
@SET /p SWAGGER_JSON=
@REM. replace C:\ by /c/
@SET PATH=%CD%
@SET PATH=%PATH:A:\=/a/%
@SET PATH=%PATH:B:\=/b/%
@SET PATH=%PATH:C:\=/c/%
@SET PATH=%PATH:D:\=/d/%
@SET PATH=%PATH:E:\=/e/%
@SET PATH=%PATH:F:\=/f/%
@SET PATH=%PATH:G:\=/g/%
@SET PATH=%PATH:H:\=/h/%
@SET PATH=%PATH:I:\=/i/%
@SET PATH=%PATH:J:\=/j/%
@SET PATH=%PATH:K:\=/k/%
@SET PATH=%PATH:L:\=/l/%
@SET PATH=%PATH:M:\=/m/%
@SET PATH=%PATH:N:\=/n/%
@SET PATH=%PATH:O:\=/o/%
@SET PATH=%PATH:P:\=/p/%
@SET PATH=%PATH:Q:\=/q/%
@SET PATH=%PATH:R:\=/r/%
@SET PATH=%PATH:S:\=/s/%
@SET PATH=%PATH:T:\=/t/%
@SET PATH=%PATH:U:\=/u/%
@SET PATH=%PATH:V:\=/v/%
@SET PATH=%PATH:W:\=/w/%
@SET PATH=%PATH:X:\=/x/%
@SET PATH=%PATH:Y:\=/y/%
@SET PATH=%PATH:Z:\=/z/%
@REM. replace \ by /
@SET PATH=%PATH:\=/%
@SET DOCKER_EXE=docker
%DOCKER_EXE% run --rm -v "%PATH%/swagger":/local swaggerapi/swagger-codegen-cli generate -i %SWAGGER_JSON% -l android -o /local
@pause
