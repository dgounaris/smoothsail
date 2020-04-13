package smoothsail.operations.git

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import smoothsail.operations.git.api.GithubJGitApi
import smoothsail.operations.git.model.GitBranchDetails
import kotlin.test.assertEquals

internal class GitBranchDetailsRetrieverTest {

    private val githubApi = mock(GithubJGitApi::class.java)
    private val gitBranchDetailsRetriever = GitBranchDetailsRetriever(githubApi)

    @Test
    fun `should return the correct object`() {
        val repo = "repo"
        val branch = "b"
        val hashcode = "1q2w3e"
        val expected = GitBranchDetails(branch, hashcode)
        `when`(githubApi.pullCheckoutBranch(repo, branch)).thenReturn(hashcode)
        assertEquals(expected, gitBranchDetailsRetriever.retrieve(repo, branch))
    }

}