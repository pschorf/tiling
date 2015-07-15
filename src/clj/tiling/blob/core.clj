(ns tiling.blob.core)

(defprotocol BlobStore
  (binary-store [this stream size] "Store blob contents, returning URI"))
