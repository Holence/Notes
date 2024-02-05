# Continual Learning

持续学习（Continual Learning or Lifelong Learning）是指训练一种可以顺序学习大量任务且不忘记先前任务习得的经验的模型，其中训练新任务时不能使用旧任务中的数据[^6e6661]。持续学习旨在防止模型的灾难性遗忘，能够在有效学习新任务的同时维持在历史任务上的表现。这里的"任务"是一种泛指，在分类任务中具体表现为：同一种类型的新的样本分布、新的类型或者新的分类任务。

在Gido M. van de Ven 和Andreas S. Tolias的研究[^1adc5f][^42d972]中，曾提出了三种持续学习的任务类型。随着学界研究的深入，出现了越来越多种类的持续学习任务。结合Vincenzo Lomonaco[^cd0d0b]与Zixuan Ke, Bing Liu[^ab86d0]可总结为Task-IL（Task Incremental Learning）、Domain-IL、Class-IL和Data-IL，见图1。图中表格内纵向根据是否有多种任务进行分类，其中$t_N$代表训练第N次时的task-id：Single-Incremental-Task中一个模型学习一个任务；Multiple-Task中一个模型需学习多个任务，学习序列中任务不重复； Multiple-Incremental-Task中，一个模型需学习多个任务，学习的序列中任务出现无限制。横向根据习得的经验进行分类：New Instances中数据种类有限，但一个Class会有新样本的出现，也就是说数据的分布（Domain）会发生改变；New Classes中数据种类会增多，但一个Class训练完后不会出现新样本，也就是说数据的分布（Domain）不发生改变；New Instances and Classes是前两者的组合，数据种类会增多，一个Class也会有新样本的出现。在多任务中的每一格又根据预测时是否需要输出task-id分成两类。

![持续学习的任务类型](<./img/持续学习的任务类型.png>)

图1. 持续学习的任务类型

这里用多语言作为多个Task，多种类型的文章作为多个Class，也假设同种任务同种类型的文章数据在不同的Batch中数据分布Domain会发生变化，用文本分类的任务举例如图2。

Task-IL任务中，训练的任务持续增多，这里三种语言为三个Task，每种语言中不同的文章类型为不同的Class，每个Task中的文章类型数量是提前确定的，且每种文章类型是一次性训练完的，所以训练的过程中每种类型的Domain不会改变。Domain-IL任务中，每种文章类型的Domain是持续改变的，三种语言的差异成了同种文章数据分布上的改变，但文章类型的数目是提前确定的。Class-IL任务中，类别的数目是持续增加的，三种语言的差异退化成了类型，因此这里就成了九种Class。Data-IL任务中，Class的数目是持续增加的，每种类型的Domain是持续改变的，所以是Domain-IL和Class-IL的混合。

![四种典型的持续学习任务的训练](<./img/四种典型的持续学习任务的训练.png>)

图2. 四种典型的持续学习任务的训练

对灾难性遗忘的一个简单解释是，当一个神经网络在一个新的任务上被训练后，它的参数会针对新的任务进行优化，而不再针对之前的任务。为了缓解灾难性遗忘，持续学习领域主要有三种方法：

1. 基于参数隔离的方法（Parameter-isolation Based Methods）

不在新任务上优化整个网络，而只优化部分网络，是缓解灾难性遗忘的一个策略。因为这种方法是针对各个任务的，所以在训练和测试中都需要task-id，因此它主要用于Task-IL的学习任务中[^1adc5f]。基于参数隔离的方法的基本思想是对每个任务使用不同的参数，主要可分为：

- Fixed Network：对于每个任务，把神经网络的参数拆分成多个子网，子网间可有重合处（XdG[^7e480b]）。

- Dynamic Architecture：对每个新任务，动态扩张出新的网络，或在一个主网络后添加不同的Adapter。

2. 基于约束的方法（Regularization Based Methods）

在损失函数中引入一个额外的约束项来减少对原网络的改变，这种方法的主要缺点是在学习新知识和预防遗忘之间无法良好地兼得。

- Data-focused：记录旧网络和一部分旧的样本，在用新网络学习新样本前，用旧网络预测旧的样本，得到一些（旧样本，预测值）的数据对，把这些数据对和新样本一起放入新网络训练，起到数据蒸馏的作用（LwF[^f14649]，iCaRL[^9bb62b]）。

- Prior-focused：在学习新数据时，先估计网络的所有参数对以前所学任务的重要性，在反向传播的时候减少对重要参数的改变（EWC[^0dcb34]）。

3. 基于回放的方法（Replay Based Methods）

在训练新任务时加入旧样本或者生成的伪样本混合训练，可以一定程度上减轻灾难性遗忘。

- Replaying Raw Samples：前人的研究中有直接存储原始样本的（ER[^27fc60]），也有利用算法筛选出少量代表性强的原始样本的（Yufan Huang et al. [^60817a]，iCaRL[^9bb62b]）。

- Replaying Generated Pseudo Samples/Features：训练一个生成网络，输入标签，生成伪样本/特征（DGR[^81c3ba]）。

![三种持续学习的方法](<./img/三种持续学习的方法.png>)

图3. 三种持续学习的方法[^42d972]。a基于参数隔离的方法，b、c基于约束的方法，d、e基于回放的方法。

在van de Ven Gido M、Tuytelaars Tinne和Tolias Andreas S.等人的研究中显示，在Task-IL的学习任务中，几乎所有的方法都表现良好，对于Domain-IL学习，许多方法的相对性能大幅下降，而对于Class-IL学习，它们的性能进一步下降。其中基于约束的方法在Domain-IL、Class-IL的学习任务上性能下降最明显，基于回放的方法在所有三种情况下都表现得相对较好[^42d972]。

对于某些场景，因为隐私和存储量的限制，不适合存储旧的样本，也不适合动态扩张网络，因此基于生成伪样本的回放方法成了比较良好的选择。

# Reference

[^113629]: Xiu. 连续学习介绍\[EB/OL\]. https://xiuyuli.com/blog/continue-learning/， 2019

[^4f607e]: 武广胜，黄松平，丁兆云等. 持续学习经典模型研究综述\[C\]. 中国指挥与控制学会（Chinese Institute of Command and Control）. 第十届中国指挥控制大会论文集（上册）. 北京: 中国指挥与控制学会，2022：236-243

[^ab86d0]: Zixuan Ke，Bing Liu. Continual Learning of Natural Language Processing Tasks: A Survey\[J\]. arXiv preprint arXiv:2211.12701，2022

[^6e6661]: Continual Learning \| Papers With Code \[EB/OL\]. https://paperswithcode.com/task/continual-learning，2023

[^1adc5f]: Gido M. van de Ven，Andreas S. Tolias. Three scenarios for continual learning.\[J\]. CoRR，2019，abs/1904.07734

[^42d972]: Van de Ven Gido M.，Tuytelaars Tinne，Tolias Andreas S.. Three types of incremental learning\[J\]. Nature Machine Intelligence，2022，4(12). doi:10.1038/S42256-022-00568-3.

[^cd0d0b]: Vincenzo Lomonaco. \[Continual Learning Course\] Lecture #3: Scenarios & Benchmarks \[EB/OL\]. https://www.youtube.com/watch?v=74C65r5qts4，2021

[^7e480b]: Nicolas Y. Masse，Gregory D. Grant，David J. Freedman. Alleviating catastrophic forgetting using context-dependent gating and synaptic stabilization\[J\]. arXiv preprint arXiv:1802.01569，2018

[^f14649]: Zhizhong Li，Derek Hoiem. Learning without forgetting\[J\]. arXiv preprint arXiv:1606.09282，2016

[^9bb62b]: Sylvestre-Alvise Rebuffi，Alexander Kolesnikov，Georg Sperl，Christoph H. Lampert. iCaRL: Incremental Classifier and Representation Learning\[J\]. arXiv preprint arXiv:1611.07725，2016

[^0dcb34]: James Kirkpatrick，Razvan Pascanu，Neil Rabinowitz，Joel Veness，Guillaume Desjardins，Andrei A. Rusu，Kieran Milan，John Quan，Tiago Ramalho，Agnieszka Grabska-Barwinska，Demis Hassabis，Claudia Clopath，Dharshan Kumaran，Raia Hadsell. Overcoming catastrophic forgetting in neural networks\[J\]. arXiv preprint arXiv:1612.00796，2016

[^27fc60]: David Rolnick，Arun Ahuja，Jonathan Schwarz，Timothy P. Lillicrap，Greg Wayne. Experience Replay for Continual Learning\[J\]. arXiv preprint arXiv:1811.11682，2018

[^60817a]: Yufan Huang，Yanzhe Zhang，Jiaao Chen，Xuezhi Wang，Diyi Yang. Continual Learning for Text Classification with Information Disentanglement Based Regularization\[J\]. arXiv preprint arXiv:2104.05489，2021

[^81c3ba]: Hanul Shin，Jung Kwon Lee，Jaehong Kim，Jiwon Kim. Continual Learning with Deep Generative Replay\[J\]. arXiv preprint arXiv:1705.08690，2017
