# -*- coding: utf-8 -*-
"""
Created on Mon Jan 23  2017

@author: ZZY
"""

import numpy as np
import random

from sklearn.datasets.samples_generator import make_blobs
from pandas import DataFrame

def genData():
  """
  generate test data for clustering algorithms using make_blob
  """
  # the number of samples
  n_samples=random.randint(50,200)
  # cluster centers
  centers=[(3,2),(5,4),(7,6)]
  # standard deviation of cluster
  cluster_std=[0.5,0.5,0.5]
  X,y=make_blobs(n_samples=n_samples,centers=centers,cluster_std=cluster_std)
  df=DataFrame(X,columns=['A','B']).astype(np.float)
  df=df.sort_values(['A'],ascending=True)
  return df

info='YOUR_PATH'
for i in range(100):
  # create test data
  filename="%d.csv"%i
  filepath=info+filename
  genData().to_csv(filepath,index=False)
  
