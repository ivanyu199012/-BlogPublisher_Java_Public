# 1. Sample Introduction

Here is the sample introduction.

![output.gif](https://cdn.hashnode.com/res/hashnode/image/upload/v1651470983404/FNd_aj4WP.gif)

## 1.1. Sample Link with image
Sample Link with Image
[Setup Django Cache](https://docs.djangoproject.com/en/4.0/ref/settings/#caches)

![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1651459141290/Lu8-l6ztU.png)


## 1.2. Sample Code Block

```python
from enum import Enum
from django.core.cache import cache
import threading
from typing import Union
from uuid import uuid1

class SampleClass:

	task_id = str

	# default constructor
	def __init__(self):
		self.task_id = str( uuid1() )
		cache.set( self.task_id, self, 3600 )

	def set( self,
		status : Enum,
		progress_message : Union[ str, None ] = None,
		output : Union[ str, None ] = None,) -> object:

		self.status = status.value
		self.progress_message = progress_message
		self.output = output

		cache.set( self.task_id, self, 3600 )

	def get_task_id( self ):
		return self.task_id

class SampleStatus(Enum):
	STARTED = 'STARTED'
	RUNNING = 'RUNNING'
	SUCCESS = 'SUCCESS'
```