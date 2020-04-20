package smoothsail.operations.jenkins.api

interface JenkinsApi {
    fun abort(jobName: String, buildNumber: Long)
}