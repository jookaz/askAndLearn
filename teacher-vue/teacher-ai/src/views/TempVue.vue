<template>
  <div class="iframe-container">
    <iframe
      src="/coze.html"
      frameborder="0"
      class="full-iframe"
    />
  </div>
</template>

<script>

</script>

<style scoped>
.iframe-container {
  height: 100vh;
  width: 100%;
}
.full-iframe {
  width: 100%;
  height: calc(100vh - 60px);
}
</style>