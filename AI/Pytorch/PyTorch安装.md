1. Update Driver
    https://www.nvidia.com/en-us/geforce/drivers/
    
    在GeForce中Update Driver，重启后看NVIDIA Control Panel中的

![cuda version](<./img/cuda version.jpg>)

![pytorch-pip](<./img/pytorch-pip.jpg>)

2. PyTorch

https://pytorch.org/get-started/locally/

检查是否能用GPU

```python
import torch
torch.cuda.is_available()
torch.cuda.device_count()
torch.rand(2,3).cuda()
```

> 不要学国内这些的
> https://zhuanlan.zhihu.com/p/345775750
>
> PyTorch自带了和底层兼容的bin，如果想自己编译Pytorch才要下载下面的东西
>
> CUDA
>
> ```
> https://developer.nvidia.com/cuda-zone
> nvcc --version 检查安装是否成功
> ```
>
> cudnn
>
> ```
> https://developer.nvidia.com/cudnn
> 压缩包内的东西复制到CUDA的根目录
> ```

3. Huggingface

```
pip install transformers
```

