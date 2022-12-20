# 1. Sample Introduction

Here is the sample introduction.

![output.gif](https://cdn.hashnode.com/res/hashnode/image/upload/v1651470983404/FNd_aj4WP.gif)

## 1.1. Sample Link with image
Sample Link with Image
[Setup Django Cache](https://docs.djangoproject.com/en/4.0/ref/settings/#caches)

![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1651459141290/Lu8-l6ztU.png)


## 1.2. Sample Code Block

``` typescript
import { spawn } from 'child_process';

const startDjangoServer = () =>
{
	const djangoBackend = spawn(`python\\edtwExampleEnv\\Scripts\\python.exe`,
		['python\\edtwExample\\manage.py', 'runserver', '--noreload']);
	djangoBackend.stdout.on('data', data =>
	{
		console.log(`stdout:\n${data}`);
	});
	djangoBackend.stderr.on('data', data =>
	{
		console.log(`stderr: ${data}`);
	});
	djangoBackend.on('error', (error) =>
	{
		console.log(`error: ${error.message}`);
	});
	djangoBackend.on('close', (code) =>
	{
		console.log(`child process exited with code ${code}`);
	});
	djangoBackend.on('message', (message) =>
	{
		console.log(`message:\n${message}`);
	});
	return djangoBackend;
}
```